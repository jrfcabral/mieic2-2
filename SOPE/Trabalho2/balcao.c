#include <unistd.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <errno.h>
#include <time.h>
#include <string.h>
#include <pthread.h>

#define SEM_NAME "/semBalcao"
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
	int temp_med_atend;
	mutex_t mutex;
}table;

typedef struct _mem_part{
	time_t data_abert_loja;
	int nBalcoes;
	char nome_sem[10];
    table tabelas[MAX_LINES];
}mem_part;

int lojaPrimUlt; //1 se e o primeiro balcao, 0 se e o ultimo, -1 se nenhum

int shmTryOpen(char *shmName){ 
	puts("opening shared memory");
	int shmFd = shm_open(shmName, (O_CREAT|O_EXCL|O_RDWR), 0777);
	if(shmFd < 0 && errno == EEXIST){ //shm already exists	
	    puts("shared memory already exists, opening it instead");    
		shmFd = shm_open(shmName, (O_RDWR), 0777);
		if(shmFd < 0){
	       perror("balcao: fatal error! couldn't open shared memory");
           return -1;
	    }
		lojaPrimUlt = -1;
	}
	else if (shmFd < 0){
	    perror("balcao:fatal error! couldn't open shared memory!");
	    return -1;
	}
	    
	else{
	    puts("shared memory created");
		lojaPrimUlt = 1;
		ftruncate(shmFd, sizeof(mem_part));	    	
	}

	return shmFd;
}

sem_t* semTryOpen(){

	sem_t* sem_id = sem_open(SEM_NAME, (O_CREAT|O_EXCL|O_RDWR), 0777, 1);
	if(sem_id == SEM_FAILED && errno == EEXIST){ //Semaphore already exists
		puts("semaphore already exists, opening it instead");
		sem_id = sem_open(SEM_NAME, (O_RDWR), 0777);
		if (sem_id == SEM_FAILED){
		    perror("balcao: fatal error! Couldn't open semaphore!");
		    exit(-1);
		}
		else{
		    int i;
		    sem_getvalue(sem_id, &i);
		    printf("opened semaphore with value %d\n", i);
		    if (i == 0)
		        sem_post(sem_id);		    
	    }
		
	}
	else if (sem_id == SEM_FAILED){
	    perror("Balcao:fatal error! couldn't open semaphore!");
	    exit(-1);
	}
	else    
		puts("semaphore created");
	
	return sem_id;
}

int initShm(mem_part *mem){
    puts("initing shared memory");    
    strncpy(mem->nome_sem, SEM_NAME,10);
    time(&(mem->data_abert_loja));  
    int i;
    for (i = 0; i < MAX_LINES;i++){
        pthread_mutex_init(&(mem->tabelas[i].mutex), NULL);
        pthread_mutex_lock(&(mem->tabelas[i].mutex));
        mem->tabelas[i].encerrado = 1;
        pthread_mutex_unlock(&(mem->tabelas[i].mutex));
    }
    
    return 0;
}

int createBalcao(mem_part *mem){
    if(mem->nBalcoes >= MAX_LINES)
        return -1;       
    table* tabela = (mem->tabelas+mem->nBalcoes);
    while(pthread_mutex_trylock(&tabela->mutex) == EBUSY){
        mem->nBalcoes++;
        tabela = mem->tabelas+mem->nBalcoes;
    }
    printf("creating balcao %d\n", mem->nBalcoes);  
    tabela->balcao = mem->nBalcoes;
    time(&(tabela->tempo));
    tabela->duracao = -1;
    tabela->encerrado = 0;
    snprintf(tabela->nome_fifo,15, "fb_%d", getpid()); 
    pthread_mutex_unlock(&tabela->mutex);
    mem->nBalcoes++;
    return (mem->nBalcoes-1);
}
void encerraBalcao(mem_part *mem, int balcao){
    pthread_mutex_lock(&mem->tabelas[balcao].mutex);
    mem->tabelas[balcao].encerrado = 1;    
    pthread_mutex_unlock(&mem->tabelas[balcao].mutex);    
}
void encerraLoja(mem_part *mem, sem_t *sem_id, char* shmName){
    int i;
    int allClosed = 1;
    for (i = 0; i < MAX_LINES && allClosed;i++)
    {
        pthread_mutex_lock(&mem->tabelas[i].mutex);
        if (!mem->tabelas[i].encerrado)
            allClosed = 0;
        pthread_mutex_unlock(&mem->tabelas[i].mutex);
    }
    if (allClosed){
        puts("Ultimo balcao a ser encerrado: vou fechar a loja");
        sem_wait(sem_id);
        shm_unlink(shmName);
        sem_post(sem_id);        
    }
    
}
int main(int argc, char **argv){

	if(argc != 3){
		printf("Usage: %s shared_mem tempo_abertura\n", argv[0]);
		exit(-1); 	
	}
	sem_t *sem_id =	semTryOpen();
	sem_wait(sem_id);
	int shared = shmTryOpen(argv[1]);
	if (shared < -1){
	    sem_post(sem_id);
	    exit(-1);
	}	    
	mem_part *mem = mmap(NULL, sizeof(mem_part), (PROT_READ|PROT_WRITE), MAP_SHARED, shared, 0);	
	close(shared);
	if (mem == MAP_FAILED){
	    perror("balcao: fatal error! Couldn't map memory: ");
	    exit(1);
	}
    if(!mem->nBalcoes)
        initShm(mem);
    int currentBalcao =  createBalcao(mem);
    if (currentBalcao < 0){
        puts("balcao: fatal error! couldn't create new table line!");
        sem_post(sem_id);
        exit(-1);        
    }
    sem_post(sem_id);
    
	char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "/tmp/fb_%d", getpid());
	mkfifo(fifoName, 0777);
	printf("fifo de atendimento criado em %s\n", fifoName);
	int fifoFd = open(fifoName, (O_RDWR), 0777);
	char buffer[20];
	while(strncmp(buffer, "close", 5) != 0){
	    read(fifoFd, (void*)buffer, 20);	    
	}
	encerraBalcao(mem, currentBalcao);
	encerraLoja(mem, sem_id, argv[1]);
    sem_close(sem_id);
    unlink(fifoName);
    if (munmap(mem, sizeof(mem_part)) == -1)
        perror("balcao: error, couldn't unmap shared memory:");
	puts("exiting normally");	
	exit(0);
}
