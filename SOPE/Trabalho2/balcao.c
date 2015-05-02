#include <unistd.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>

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
	if(shmFd < 0){ //shm already exists
		shmFd = shm_open(shmName, (O_RDWR), 0660);
		lojaPrimUlt = -1;
	}
	else{
		lojaPrimUlt = 1;
		ftruncate(shmFd, sizeof(mem_part));	
	}
	return shmFd;
}

int semTryOpen(){
	sem_id = sem_open(SEM_NAME, (O_CREAT|O_EXCL|O_RDWR), 0660);
	if(sem_id < 0){ //Semaphore already exists
		sem_id = sem_open(SEM_NAME, (O_RDWR), 0660);
	}
	return 0;
}

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: %s shared_mem tempo_abertura\n", argv[0]);
		exit(-1); 	
	}
	

	semTryOpen();

	sem_wait(sem_id);
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
}
