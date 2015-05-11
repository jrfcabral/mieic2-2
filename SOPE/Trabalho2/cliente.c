#include <unistd.h>
#include <stdio.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>

#define MAX_LINES 100
typedef pthread_mutex_t mutex_t;

typedef struct _table{
	int balcao;
	int encerrado;
	time_t tempo;
	int duracao;
	char nome_fifo[15];
	int em_atendimento;
	int ja_atendidos;
	int tempo_med_atend;
	mutex_t mutex;
}table;

typedef struct _mem_part{
	time_t data_abert_loja;
	int nBalcoes;
	int balcoesDisponiveis;
	char nome_sem[10];
    table tabelas[MAX_LINES];
}mem_part;


int shmTryOpen(char *mem_part){
	int shmFd = shm_open(mem_part, O_RDWR, 0777);
	if(errno == ENOENT){
		perror("Shared memory appears not to exist. Exiting.\n");
		exit(-1);
	}
	
	return shmFd;
}


char *mkClientFifo(){
	char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "fc_%d", getpid());
	mkfifo(fifoName, 0777);
	return fifoName;
}


int genClient(mem_part *mem){
	char *fifo = mkClientFifo();
	int j = 0, minClientes = 9999999;
	for(j = 0; j < mem->nBalcoes; j++){
		int atend = mem->tabelas[j].em_atendimento;
		if(atend < minClientes){
			minClientes = atend;
		}
	}
	int clienteFifo = open(fifo, O_RDWR, 0777);
	if(clienteFifo < 0){
		perror("Could not open client FIFO. Exiting.\n");
		exit(-1);
	}
	int balcaoFifo = open(mem->tabelas[minClientes].nome_fifo, O_RDWR, 0777);
	if(balcaoFifo < 0){
		perror("Could not open counter FIFO. Exiting.");
		exit(-1);
	}
	write(balcaoFifo, fifo, strlen(fifo));
	int atendido = 0;
	char *atendimento = (char *)malloc(20*sizeof(char)+1);	
	while(!atendido){
		int n = read(clienteFifo, atendimento, 20);
		if(!strcmp(atendimento, "fim_atendimento")){
			atendido = 1;
		}		
	}
	return 0;
}

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: %s mem_partilhada num_clientes", argv[0]);
		exit(-1);
	}

	int shm = shmTryOpen(argv[1]);
	
	mem_part *mem = mmap(NULL, sizeof(mem_part), (PROT_READ|PROT_WRITE), MAP_SHARED, shm, 0);	
	close(shm);
	if(mem == MAP_FAILED){
		perror("Could not map shared memory. Exiting.\n");
		exit(-1);
	}


	int i;
	for(i = 0; i < atoi(argv[2]); i++){
		pid_t pid = fork();
		if(pid < 0){
			perror("Generation of clients failed. Exiting.\n");
			exit(-1);
		}
		else if(pid == 0){ //Filho
			genClient(mem);
			exit(0);
		}
	}
}