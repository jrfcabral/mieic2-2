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
#include <limits.h>

#include "loja.h"




int generateStats(sem_t *sem_id, mem_part *mem){
	int statFile = open("statistics.txt", (O_CREAT|O_TRUNC|O_RDWR), 0777);
	int totClients = 0, tempoMedAtend = 0, i;
	int **totClientsBalcao = (int **) malloc(mem->nBalcoes*sizeof(int*));
	int **tempoMedAtendBalcao = (int **)malloc(mem->nBalcoes*sizeof(int*));
	int duracaoLoja;
	int **duracaoBalcao = (int **)malloc(mem->nBalcoes*sizeof(int*));	

	char *totalClients = (char *)malloc(100*sizeof(char));
	char **totalClientsBalcao = (char **)malloc(mem->nBalcoes*sizeof(char*));
	char *tempoMedioAtend = (char *)malloc(100*sizeof(char));
	char **tempoMedioAtendBalcao = (char **)malloc(mem->nBalcoes*sizeof(char));
	char *durLoja = (char *)malloc(100*sizeof(char));
	char **durBalcao = (char **)malloc(mem->nBalcoes*sizeof(char));


	sem_wait(sem_id);
	for(i = 0; i < mem->nBalcoes; i++){
		totClientsBalcao[i] = malloc(sizeof(int));
		tempoMedAtendBalcao[i] = malloc(sizeof(int));
		duracaoBalcao[i] = malloc(sizeof(int));
		
		totClients += mem->tabelas[i].ja_atendidos;
		tempoMedAtend += mem->tabelas[i].tempo_med_atend;
		*totClientsBalcao[i] = mem->tabelas[i].ja_atendidos;
		*tempoMedAtendBalcao[i] = mem->tabelas[i].tempo_med_atend;
		*duracaoBalcao[i] = mem->tabelas[i].duracao;
		
	}
	tempoMedAtend /= mem->nBalcoes;
	printf("encerrar - %d - %d\n", mem->data_abert_loja, time(NULL));
	
	duracaoLoja = time(NULL) - mem->data_abert_loja;

	sem_post(sem_id);
	
	printf("Total de Clientes: %d\n", totClients);
	sprintf(totalClients, "Total de Clientes: %d\n", totClients);
	write(statFile, totalClients, strlen(totalClients));
	free(totalClients);
	
	for(i = 0; i < mem->nBalcoes; i++){
		printf("Clientes no balcao %d : %d\n", i, *totClientsBalcao[i]);
		totalClientsBalcao[i] = malloc(50*sizeof(char));
		sprintf(totalClientsBalcao[i], "Clientes no balcao %d : %d\n", i, *totClientsBalcao[i]);
		write(statFile, totalClientsBalcao[i], strlen(totalClientsBalcao[i]));
		free(totalClientsBalcao[i]);
		free(totClientsBalcao[i]);
	}
	
	printf("Tempo medio de atendimento: %d\n", tempoMedAtend);
	sprintf(tempoMedioAtend, "Tempo medio de atendimento: %d\n", tempoMedAtend);
	write(statFile, tempoMedioAtend, strlen(tempoMedioAtend));
	free(tempoMedioAtend);

	for(i = 0; i < mem->nBalcoes; i++){
		tempoMedioAtendBalcao[i] = malloc(100*sizeof(char));
		printf("Tempo medio de atendimento no balcao %d: %d\n", i, *tempoMedAtendBalcao[i]);
		sprintf(tempoMedioAtendBalcao[i], "Tempo medio de atendimento no balcao%d: %d\n", i, *tempoMedAtendBalcao[i]);
		write(statFile, tempoMedioAtendBalcao[i], strlen(tempoMedioAtendBalcao[i]));
		free(tempoMedioAtendBalcao[i]);
		free(tempoMedAtendBalcao[i]);
		
	}

	printf("Tempo de abertura da loja: %d\n", duracaoLoja);
	sprintf(durLoja, "Tempo de abertura da loja: %d\n", duracaoLoja);
	write(statFile, durLoja, strlen(durLoja));
	free(durLoja);

	for(i = 0; i < mem->nBalcoes; i++){
		durBalcao[i] = malloc(100*sizeof(char));
		printf("Tempo de abertura do balcao %d: %d\n", i, *duracaoBalcao[i]);
		sprintf(durBalcao[i], "Tempo de abertura do balcao %d: %d\n", i, *duracaoBalcao[i]);
		write(statFile, durBalcao[i], strlen(durBalcao[i]));
		free(durBalcao[i]);
		free(duracaoBalcao[i]);
	}
	

	close(statFile);
	return 0;
}


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
	}
	else if (shmFd < 0){
	    perror("balcao:fatal error! couldn't open shared memory!");
	    return -1;
	}
	    
	else{
	    puts("shared memory created");

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
    strncpy(mem->nome_sem, SEM_NAME,strlen(SEM_NAME)+1);
    time(&(mem->data_abert_loja));  
    int i;
    for (i = 0; i < MAX_LINES;i++){
        pthread_mutexattr_t attrmutex;
        pthread_mutexattr_init(&attrmutex);
        pthread_mutexattr_setpshared(&attrmutex, PTHREAD_PROCESS_SHARED);
        pthread_mutex_init(&(mem->tabelas[i].mutex), &attrmutex);
        pthread_mutexattr_destroy(&attrmutex);
        pthread_mutex_lock(&(mem->tabelas[i].mutex));
        mem->tabelas[i].encerrado = 1;
        pthread_mutex_unlock(&(mem->tabelas[i].mutex));
    }
    mem->data_abert_loja = time(NULL);
    printf("%d\n", mem->data_abert_loja);
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
    //tabela->tempo = time(NULL);
    snprintf(tabela->nome_fifo,15, "/tmp/fb_%d", getpid()); 
    pthread_mutex_unlock(&tabela->mutex);
    mem->nBalcoes++;
    mem->balcoesDisponiveis++;
    return (mem->nBalcoes-1);
}
void encerraBalcao(mem_part *mem, int balcao, sem_t* sem_id){
    pthread_mutex_lock(&mem->tabelas[balcao].mutex);
    mem->tabelas[balcao].encerrado = 1; 
    mem->tabelas[balcao].duracao = time(NULL) - mem->tabelas[balcao].tempo;
    pthread_mutex_unlock(&mem->tabelas[balcao].mutex);
   	sem_wait(sem_id);
	mem->balcoesDisponiveis--;
	sem_post(sem_id);    
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
		generateStats(sem_id, mem);
        puts("Ultimo balcao a ser encerrado: vou fechar a loja");
        sem_wait(sem_id);
        shm_unlink(shmName);
        sem_post(sem_id);
        sem_unlink(mem->nome_sem);        
    }
    
}
void* atendimento(void* arg){
    infoAtendimento* info = (infoAtendimento*) arg;
    table *tabela = &info->mem->tabelas[info->balcaoNumber];
    pthread_mutex_lock(&tabela->mutex);
    printf("%s a atender cliente cujo fifo privado é %s", tabela->nome_fifo, info->fifoName);
    int duracao = tabela->em_atendimento +1;
    printf("e vai esperar %d segundos\n", duracao);
    tabela->em_atendimento++;
    pthread_mutex_unlock(&info->mem->tabelas[info->balcaoNumber].mutex);
    sleep(duracao);
    pthread_mutex_lock(&tabela->mutex);
    int fifo = open(info->fifoName, O_WRONLY, 0777);
    char mensagem[] = "fim_atendimento";
    write(fifo, mensagem, strlen(mensagem));
	
    printf("%s atendido cliente cujo fifo privado é %s\n", tabela->nome_fifo, info->fifoName);
    tabela->tempo_med_atend = ( (tabela->tempo_med_atend*(float)tabela->ja_atendidos + (float)duracao)/((float)tabela->ja_atendidos+1) );
    printf("tempo médio %f\n", tabela->tempo_med_atend);
    tabela->em_atendimento--;   
    tabela->ja_atendidos++;
    pthread_mutex_unlock(&tabela->mutex);
	 close(fifo);
    free(arg);
    return NULL;
}

void* alarme(void* arg){
    infoAlarme* info = (infoAlarme*) arg;
    printf("vou esperar %d segundos\n", info->tempo);
    sleep(info->tempo);
    char *fifoName = (char *)malloc(15*sizeof(char));
	 sprintf(fifoName, "/tmp/fb_%d", getpid());
    int fifo = open(fifoName, O_WRONLY, 0777);
    char message[] = "close";
    write(fifo, message, strlen(message));
    return(NULL);    
    
}
int main(int argc, char **argv){

	if(argc != 3){
		printf("Usage: %s shared_mem tempo_abertura\n", argv[0]);
		exit(-1); 	
	}
    long int duracao;
    if ( ((duracao = strtol(argv[2], NULL, 10)) < 1) || (duracao == LONG_MAX && errno == ERANGE) )
    {
        printf("balcao: fatal error, invalid duration \"%s\" provided!", argv[2]);
        if (duracao == 0)
            puts(" Duration must be an integer > 0.");
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
	    sem_post(sem_id);
	    exit(1);
	}
    if(!mem->nBalcoes)
        initShm(mem);        
    int currentBalcao =  createBalcao(mem);
    if (currentBalcao < 0){
        puts("balcao: fatal error! couldn't create new table line! Store is probably full.");
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
	pthread_t *clients = malloc(sizeof(pthread_t));
    pthread_t alarme_thread;
    infoAlarme alarmeConfig;
    alarmeConfig.tempo = duracao;
    alarmeConfig.balcaoNumber = currentBalcao;
    pthread_create(&alarme_thread, NULL, alarme, (void*)&alarmeConfig);
	int clientsSize = 0;
	while(strncmp(buffer, "close", 6) != 0){
        memset(buffer, 0, 20);
	    read(fifoFd, (void*)buffer, 20);
        puts("read from fifo");
        puts(buffer);
	    if (strncmp(buffer, "/tmp/fc", 7) == 0)
	    {
            puts("atender");
	        clientsSize++;
	        if ( (clients = realloc(clients, clientsSize*sizeof(pthread_t))) == NULL){
	            perror("balcao: couldn't allocate thread space");
	            exit(-1);
	        }
	        infoAtendimento* info = malloc(sizeof(infoAtendimento));//a ser libertado pelo thread
	        strncpy(info->fifoName, buffer, 20);
	        info->balcaoNumber = currentBalcao;
	        info->mem = mem;
	        
	        pthread_create(&clients[clientsSize-1], NULL, atendimento, (void*)info);
	    }        
	}
    
    int i;
    for(i = 0; i < clientsSize; i++)
        pthread_join(clients[i], NULL);
    
	encerraBalcao(mem, currentBalcao, sem_id);
	encerraLoja(mem, sem_id, argv[1]);
   sem_close(sem_id);
   unlink(fifoName);
   if (munmap(mem, sizeof(mem_part)) == -1)
       perror("balcao: error, couldn't unmap shared memory:");
	puts("exiting normally");	
	exit(0);
}
