#include <unistd.h>
#include <stdio.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <pthread.h>
#include <semaphore.h>
#include "loja.h"

void exitClient(int msg){
	char *fifoName = (char *)malloc(20*sizeof(char));
	sprintf(fifoName, "/tmp/fc_%d", getpid()); 
	unlink(fifoName);
	free(fifoName);
	exit(msg);
}


int shmTryOpen(char *mem_part){
	int shmFd = shm_open(mem_part, O_RDWR, 0777);
	if(errno == ENOENT){
		perror("Shared memory appears not to exist. Exiting.\n");
		exit(-1);
	}
	
	return shmFd;
}

sem_t* semTryOpen(mem_part *mem){
	sem_t *sem_id;
	sem_id = sem_open(mem->nome_sem, (O_RDWR), 0777);
	if (sem_id == SEM_FAILED){
	    perror("cliente: fatal error! Couldn't open semaphore!");
	    exit(-1);
	}
	else{
	    int i;
	    sem_getvalue(sem_id, &i);
	}
	return sem_id;
}



char *mkClientFifo(){
	char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "/tmp/fc_%d", getpid());
	mkfifo(fifoName, 0777);	
	return fifoName;
}


int genClient(mem_part *mem, sem_t *sem){
    alarm(CLIENTE_TIMEOUT);	
	char *fifo = mkClientFifo();
	int j = 0, minClientes = 9999999, minBalcao = 0;
	sem_wait(sem);	
	for(j = 0; j < mem->nBalcoes; j++){		
		pthread_mutex_lock(&mem->tabelas[j].mutex);
		int atend = mem->tabelas[j].em_atendimento;
		if(atend <= minClientes && mem->tabelas[j].encerrado == 0){
  			minClientes = atend;
		   minBalcao = j;			
		}
   	pthread_mutex_unlock(&mem->tabelas[j].mutex);
	}
	sem_post(sem);
	int clienteFifo = open(fifo, O_RDWR, 0777);	
	if(clienteFifo < 0){
		puts(fifo);
		perror("Could not open client FIFO. Exiting.\n");
		exitClient(-1);
	}
	
	//preparar mensagem
	mensagemBalcao msg;
	msg.close = 0;
	memset(msg.fifoName, 0, 100);
	strncpy(msg.fifoName, fifo, 100);
	char* nomeFifo = malloc(strlen(mem->tabelas[minBalcao].nome_fifo)*sizeof(char)+1);
	pthread_mutex_lock(&mem->tabelas[minBalcao].mutex);		
	int balcaoFifo = open(mem->tabelas[minBalcao].nome_fifo, O_WRONLY, 0777);
	pthread_mutex_unlock(&mem->tabelas[minBalcao].mutex);
	free(nomeFifo);	
	if(balcaoFifo < 0){				
		perror("Could not open counter FIFO. Exiting.");		
		exitClient(-1);
	}
	
	 
		
	write(balcaoFifo, &msg, sizeof(mensagemBalcao));
	printLog(mem->nome_mem, "Cliente", minBalcao, "pede_atendimento", fifo, &mem->logmutex);
	//esperar por atendimento		
	int atendido = 0;
	char *atendimento = (char *)malloc(20*sizeof(char)+1);	
	while(!atendido){
		int n = read(clienteFifo, atendimento, 20);
		if(!strncmp(atendimento, "fim_atendimento",n)){
			atendido = 1;
			printLog(mem->nome_mem, "Cliente", minBalcao, "fim_atendimento", fifo, &mem->logmutex);			
		}		
	}
	free(atendimento);
	free(fifo);	
	return 0;
}

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: %s mem_partilhada num_clientes", argv[0]);
		exit(-1);
	}

	int shm = shmTryOpen(argv[1]);
	sem_t *sem;	

	mem_part *mem = mmap(NULL, sizeof(mem_part), (PROT_READ|PROT_WRITE), MAP_SHARED, shm, 0);	
	close(shm);
	if(mem == MAP_FAILED){
		perror("Could not map shared memory. Exiting.\n");
		exit(-1);
	}

	sem = semTryOpen(mem);

	int i;
	for(i = 0; i < atoi(argv[2]); i++){
		pid_t pid = fork();
		if(pid < 0){
			perror("Generation of clients failed. Exiting.\n");
			exit(-1);
		}
		else if(pid == 0){ //Filho
			genClient(mem, sem);
			sem_close(sem);
			if ( ( munmap(mem, sizeof(mem_part)) ) == -1)
				perror("ger_cl:couldn't unmap shared memory");		
			exitClient(0);
		}
	}
	sem_close(sem);
	if ( ( munmap(mem, sizeof(mem_part)) ) == -1)
		perror("ger_cl:couldn't unmap shared memory");
	exit(0);
}
