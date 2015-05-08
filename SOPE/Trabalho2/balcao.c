#include <unistd.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <errno.h>

#define SEM_NAME "/semBalcao"

typedef struct _table{
	int balcao;
	int tempo;
	int duracao;
	char nome_fifo[10];
	int em_atendimento;
	int ja_atendidos;
	int temp_med_atend;
}table;

typedef struct _mem_part{
	int data_abert_loja;
	int nBalcoes;
	char nome_sem[10];
	table *tabela;
}mem_part;

int lojaPrimUlt; //1 se e o primeiro balcao, 0 se e o ultimo, -1 se nenhum
sem_t *sem_id;


int shmTryOpen(char *shmName){ //FALTAM SEMAFOROS E/OU MUTEXES
	int shmFd = shm_open(shmName, (O_CREAT|O_EXCL|O_RDWR), 0660);
	if(shmFd < 0 && errno == EEXIST){ //shm already exists	
	    puts("shared memory already exists, opening it instead");    
		shmFd = shm_open(shmName, (O_RDWR), 0660);
		lojaPrimUlt = -1;
	}
	else if(shmFd < 0){
	    puts("balcao: fatal error! couldn't open shared memory");
	    perror(NULL);
	}
	else{
	    puts("shared memory created");
		lojaPrimUlt = 1;
		ftruncate(shmFd, sizeof(mem_part));	    	
	}
	return shmFd;
}

int semTryOpen(){

	sem_id = sem_open(SEM_NAME, (O_CREAT|O_EXCL|O_RDWR), 0660, 1);
	if(sem_id == SEM_FAILED && errno == EEXIST){ //Semaphore already exists
		puts("semaphore already exists, opening it instead");
		sem_id = sem_open(SEM_NAME, (O_RDWR), 0660);
		if (sem_id == SEM_FAILED){
		    puts("balcao: fatal error! Couldn't open semaphore!");
		    exit(-1);
		}
		else{
		    int i;
		    sem_getvalue(sem_id, &i);
		    printf("opened semaphore with value %d\n", i);		    
	    }
		
	}
	else if (sem_id == SEM_FAILED){
	    perror("Balcao:fatal error!");
	    exit(-1);
	}
	else
		puts("semaphore created");
	
	return 0;
}

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: %s shared_mem tempo_abertura\n", argv[0]);
		exit(-1); 	
	}
	

	semTryOpen();
	puts("sem openned");

	 int i;
		    sem_getvalue(sem_id, &i);
		    printf("opened semaphore with value %d\n", i);
	sem_wait(sem_id);
	puts("got past semaphore");
	int shared = shmTryOpen(argv[1]);
	sem_post(sem_id);
	
	mem_part *mem = mmap(NULL, sizeof(mem_part), (PROT_READ|PROT_WRITE), MAP_SHARED, shared, 0);
	
	close(shared);

	if(lojaPrimUlt == -1){
		if(mem->nBalcoes == 1){ //Este e o ultimo balcao
			lojaPrimUlt = 0;				
			/*Gerar cenas e coise ao despois*/
		}
	}
	else if(lojaPrimUlt == 1){
		sem_wait(sem_id);
		/*Gerar cenas e coise*/
		sem_post(sem_id);
	}		


	char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "/tmp/fb_%d", getpid());
	mkfifo(fifoName, 0660);
	puts("exiting normally");
	exit(0);
}
