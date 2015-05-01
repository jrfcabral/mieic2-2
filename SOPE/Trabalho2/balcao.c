#include <unistd.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct _table{
	int balcao;
	int tempo;
	int duracao;
	char *nome_fifo;
	int em_atendimento;
	int ja_atendidos;
	int temp_med_atend;
}table;

typedef struct _mem_part{
	int data_abert_loja;
	int nBalcoes;
	table *tabela;
}mem_part;

int shmTryOpen(char *shmName){ //FALTAM SEMAFOROS E/OU MUTEXES
	int shmFd = shm_open(shmName, (O_CREAT|O_EXCL|O_RDWR), 0660);
	if(shmFd < 0){ //shm already exists
		shmFd = shm_open(shmName, (O_RDWR), 0660);
	}
	else{
		
		ftruncate(shmFd, 1000);	
	}
	return shmFd;
}

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: %s shared_mem tempo_abertura\n", argv[0]);
		exit(-1); 	
	}

	int shared = shmTryOpen(argv[1]);
		


	char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "/tmp/fb_%d", getpid());
	mkfifo(fifoName, 0660);
}
