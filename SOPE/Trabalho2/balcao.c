#include "loja.h"


int genStats(sem_t *sem_id, mem_part *mem, char *shmName){
	char *statFileName = (char *)malloc(strlen(shmName)+1+strlen("statistics.txt")+1*sizeof(char));
	strcpy(statFileName, shmName+1);
	strcat(statFileName, "Statistics.txt"); 
	puts(statFileName);
	int statFile = open(statFileName, (O_CREAT|O_TRUNC|O_RDWR), 0777);
	if(statFile < 0){
		perror("Couldn't open stat file: ");
	}

	int totClients = 0, i;
	float totAvgTendTime = 0;


	for(i = 0; i < mem->nBalcoes; i++){
		char *totalClientsPerBalcon = (char *)malloc(100*sizeof(char));
		totClients += mem->tabelas[i].ja_atendidos;
		sprintf(totalClientsPerBalcon, "Clientes atendidos no balcao %d: %d\n", i, mem->tabelas[i].ja_atendidos);
		write(statFile, totalClientsPerBalcon, strlen(totalClientsPerBalcon));
		free(totalClientsPerBalcon);
	}
		
	char *totalClients = (char *)malloc(100*sizeof(char));
	sprintf(totalClients, "Total de Clientes: %d\n\n", totClients);
	write(statFile, totalClients, strlen(totalClients));	
	free(totalClients);

	for(i = 0; i < mem->nBalcoes; i++){
		char *avgTendTimePerBalcon = (char *)malloc(100*sizeof(char));
		totAvgTendTime += mem->tabelas[i].tempo_med_atend;
		sprintf(avgTendTimePerBalcon, "Tempo medio de atendimento no balcao %d: %.2f\n", i, mem->tabelas[i].tempo_med_atend);
		write(statFile, avgTendTimePerBalcon, strlen(avgTendTimePerBalcon));
		free(avgTendTimePerBalcon);		
	}
	
	char *totalAvgTendTime = (char *)malloc(100*sizeof(char));
	sprintf(totalAvgTendTime, "Tempo medio de atendimento na loja: %.2f\n\n", (totAvgTendTime/(float)mem->nBalcoes));
	write(statFile, totalAvgTendTime, strlen(totalAvgTendTime));
	free(totalAvgTendTime);

	for(i = 0; i < mem->nBalcoes; i++){
		char *timeRemainedOpenBalcon = (char *)malloc(100*sizeof(char));
		sprintf(timeRemainedOpenBalcon, "Duracao de abertura do balcao %d: %d\n", i, mem->tabelas[i].duracao);
		write(statFile, timeRemainedOpenBalcon, strlen(timeRemainedOpenBalcon));
		free(timeRemainedOpenBalcon);
	}

	char *timeRemainedOpenStore = (char *)malloc(100*sizeof(char));
	int duration = time(NULL) - mem->data_abert_loja;
	sprintf(timeRemainedOpenStore, "Duracao de abertura da loja: %d\n\n", duration);
	write(statFile, timeRemainedOpenStore, strlen(timeRemainedOpenStore));
	free(timeRemainedOpenStore);

	for(i = 0; i < mem->nBalcoes; i++){
		struct tm time;
		localtime_r(&(mem->tabelas[i].tempo), &time);
		char *timeOfCreationBalcon = (char *)malloc(100*sizeof(char));
		sprintf(timeOfCreationBalcon, "Data de abertura do balcao %d: %d/%d/%d %d:%d:%d\n", i, time.tm_mday, time.tm_mon, time.tm_year+1900, time.tm_hour, time.tm_min, time.tm_sec);
		write(statFile, timeOfCreationBalcon, strlen(timeOfCreationBalcon));
		free(timeOfCreationBalcon);
	}
	
	struct tm time;
	localtime_r(&(mem->data_abert_loja), &time);
	char *timeOfCreationStore = (char *)malloc(100*sizeof(char));
	sprintf(timeOfCreationStore, "Data de abertura da loja: %d/%d/%d %d:%d:%d\n", time.tm_mday, time.tm_mon, time.tm_year+1900, time.tm_hour, time.tm_min, time.tm_sec);
	write(statFile, timeOfCreationStore, strlen(timeOfCreationStore));
	free(timeOfCreationStore);
	
	close(statFile);
	free(statFileName);
	return 0;
}


int shmTryOpen(char *shmName){ 

	int shmFd = shm_open(shmName, (O_CREAT|O_EXCL|O_RDWR), 0666);
	if(shmFd < 0 && errno == EEXIST){ //shm already exists	

		shmFd = shm_open(shmName, (O_RDWR), 0666);
		if(shmFd < 0){
	       perror("balcao: fatal error! couldn't open shared memory");
           return -1;
	    }
	}
	else if (shmFd < 0){
	    perror("balcao:fatal error! couldn't open shared memory!");
	    return -1;
	}
	    
	else
		ftruncate(shmFd, sizeof(mem_part));	    	
	

	return shmFd;
}

sem_t* semTryOpen(char *shmName){
   	
	char *semName = (char *)malloc(strlen(shmName)+5*sizeof(char));
	strcpy(semName, shmName);
	strcat(semName, "Sem");

   sem_t *sem_id = sem_open(semName, O_CREAT | O_RDWR |O_EXCL, 0666, 1);
   //if the semaphore already exists open it
   if (sem_id == SEM_FAILED && errno == EEXIST){        
        sem_id = sem_open(semName, (O_RDWR),0666);
    
   }
   //if it still can't be opened or couldn't be opened to begin with, print error
   if (sem_id == SEM_FAILED){
        perror("Failed to open semaphore");
        exit(-1);        	
   }
   free(semName);
   return sem_id;
} 

int initShm(mem_part *mem, char* shmName){
    printLog(shmName+1, "Balcao ", 0, "inicializa_mempart", "-", &mem->logmutex);
    
	
	char *semName = (char *)malloc(strlen(shmName)+5*sizeof(char));
	strcpy(semName, shmName);
	strcat(semName, "Sem");
	    
    strncpy(mem->nome_sem, semName, strlen(semName)+1);
    time(&(mem->data_abert_loja));
    strncpy(mem->nome_mem, shmName+1, strlen(shmName)+1);
    pthread_mutexattr_t attrmutex;
    pthread_mutexattr_init(&attrmutex);
    pthread_mutexattr_setpshared(&attrmutex, PTHREAD_PROCESS_SHARED);
    pthread_mutex_init(&mem->logmutex, &attrmutex);  
    int i;
    for (i = 0; i < MAX_LINES;i++){
        pthread_mutex_init(&(mem->tabelas[i].mutex), &attrmutex);

        pthread_mutex_lock(&(mem->tabelas[i].mutex));
        mem->tabelas[i].encerrado = 1;
        pthread_mutex_unlock(&(mem->tabelas[i].mutex));
    }
    mem->data_abert_loja = time(NULL);
    pthread_mutexattr_destroy(&attrmutex);
	free(semName);
	
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
    tabela->balcao = mem->nBalcoes;
    time(&(tabela->tempo));
    tabela->duracao = -1;
    tabela->encerrado = 0;
    snprintf(tabela->nome_fifo,15, "/tmp/fb_%d", getpid()); 
    pthread_mutex_unlock(&tabela->mutex);
    mem->nBalcoes++;
    mem->balcoesDisponiveis++;
    printLog(mem->nome_mem, "Balcao ", mem->nBalcoes-1, "cria_linha_mempart", tabela->nome_fifo, &mem->logmutex);
    return (mem->nBalcoes-1);
}
void encerraBalcao(mem_part *mem, int balcao, sem_t* sem_id){
    pthread_mutex_lock(&mem->tabelas[balcao].mutex);
    mem->tabelas[balcao].encerrado = 1; 
    mem->tabelas[balcao].duracao = time(NULL) - mem->tabelas[balcao].tempo;
    pthread_mutex_unlock(&mem->tabelas[balcao].mutex);
   	sem_wait(sem_id);
	mem->balcoesDisponiveis--;
    printLog(mem->nome_mem, "Balcao", balcao, "fecha_balcao", mem->tabelas[balcao].nome_fifo, &mem->logmutex);
	sem_post(sem_id);    

}
void encerraLoja(mem_part *mem, sem_t *sem_id, char* shmName, int balcao){
    int i;
    int allClosed = 1;
    for (i = 0; i < MAX_LINES && allClosed;i++)
    {
        pthread_mutex_lock(&mem->tabelas[i].mutex);
        if (!mem->tabelas[i].encerrado)
            allClosed = 0;
        pthread_mutex_unlock(&mem->tabelas[i].mutex);
    }
    sem_wait(sem_id);    
    if (mem->balcoesDisponiveis == 0){
	genStats(sem_id, mem, shmName);
        puts("Ultimo balcao a ser encerrado: a encerrar loja");
		puts("Estatisticas disponiveis no ficheiro [nome mem_partilhada]statistics.txt");
        printLog(mem->nome_mem, "Balcao", balcao, "fecha_loja", mem->tabelas[balcao].nome_fifo, &mem->logmutex);        
        shm_unlink(shmName);
        sem_post(sem_id);
        sem_unlink(mem->nome_sem);
        
    }
    else
        sem_post(sem_id);
    
}
void* atendimento(void* arg){
    infoAtendimento* info = (infoAtendimento*) arg;
    table *tabela = &info->mem->tabelas[info->balcaoNumber];
    pthread_mutex_lock(&tabela->mutex);

    
    int duracao = tabela->em_atendimento +1;
	 if(duracao > 10)
	 	duracao = 10;
    

    tabela->em_atendimento++;
    pthread_mutex_unlock(&info->mem->tabelas[info->balcaoNumber].mutex);
    printLog(info->mem->nome_mem, "Balcao", info->balcaoNumber, "inicia_atend_cli", info->fifoName, &info->mem->logmutex);
    sleep(duracao);
    pthread_mutex_lock(&tabela->mutex);
    int fifo = open(info->fifoName, O_WRONLY | O_NONBLOCK, 0666);        
    if (fifo < 0)
    {     
       // perror("Couldn't open client fifo, it probably already ceased to exist by now");
        free(arg);
        pthread_mutex_unlock(&tabela->mutex);
        pthread_exit(NULL);
        
    }
    char mensagem[] = "fim_atendimento";
    write(fifo, mensagem, strlen(mensagem));
	
    tabela->tempo_med_atend = ( (tabela->tempo_med_atend*(float)tabela->ja_atendidos + (float)duracao)/((float)tabela->ja_atendidos+1) );
    tabela->em_atendimento--;   
    tabela->ja_atendidos++;
    pthread_mutex_unlock(&tabela->mutex);
	close(fifo);
    free(arg);
    pthread_exit(NULL);

}

void* alarme(void* arg){
    pthread_detach(pthread_self());
    infoAlarme* info = (infoAlarme*) arg;    
    sleep(info->tempo);
    
    char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "/tmp/fb_%d", getpid());
    int fifo = open(fifoName, O_WRONLY, 0666);
    
    mensagemBalcao msg;
    msg.close = 1;
    
    write(fifo, &msg, sizeof(mensagemBalcao));
    
    free(fifoName);
    return(NULL);    
    
}
int main(int argc, char **argv){
    //verifica e valida argumentos do programa
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
     if (*argv[1] != '/'){
            printf("balcao: fatal error, invalid memory name \"%s\" provided! Memory name must start with a /", argv[2]);
            exit(-1);
    }
   
    
    //alterar a umask para permitir que outros utilizadores acedam aos recursos criados por este process
    umask((mode_t) 0000);
    //abrir/criar e mapear memoria partilhada e abrir/criar o semaforo com nome
	sem_t *sem_id =	semTryOpen(argv[1]);
    if(sem_id == SEM_FAILED){
        perror("Couldn't open semaphore");
        exit(-1);
    }
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
        initShm(mem, argv[1]);                
    
    //criar um novo balcao
    int currentBalcao =  createBalcao(mem);
    if (currentBalcao < 0){
        puts("balcao: fatal error! couldn't create new table line! Store is probably full.");
        sem_post(sem_id);
        exit(-1);        
    }
    sem_post(sem_id);
    
    //abre o fifo de atendimento    
	char *fifoName = (char *)malloc(15*sizeof(char));
	sprintf(fifoName, "/tmp/fb_%d", getpid());
	mkfifo(fifoName, 0666);	
	int fifoFd = open(fifoName, (O_RDWR), 0666);	
    
    //prepara array que vai receber a informação dos threads de atendimento
	pthread_t *clients = malloc(sizeof(pthread_t));
    int clientsSize = 0;
    
    //configura um alarme para encerrar o balcao
    pthread_t alarme_thread;
    infoAlarme alarmeConfig;
    alarmeConfig.tempo = duracao;
    alarmeConfig.balcaoNumber = currentBalcao;
    pthread_create(&alarme_thread, NULL, alarme, (void*)&alarmeConfig);
        
	

    //le as mensagens recebidas e gera threads de atendimento
    mensagemBalcao msg;
    msg.close = 0;
	while(!msg.close){
        
	    read(fifoFd, (void*)&msg, sizeof(mensagemBalcao));
	    if (!msg.close)
	    {           
	        clientsSize++;
	        if ( (clients = realloc(clients, clientsSize*sizeof(pthread_t))) == NULL){
	            perror("balcao: couldn't allocate thread space");
	            exit(-1);
	        }
	        infoAtendimento* info = malloc(sizeof(infoAtendimento));//a ser libertado pelo thread
	        strncpy(info->fifoName, msg.fifoName, 100);
	        info->balcaoNumber = currentBalcao;
	        info->mem = mem;
	        
	        if(pthread_create(&clients[clientsSize-1], NULL, atendimento, (void*)info)){
	        clientsSize--;
			free(info);
			perror("Couldn't start new thread:");
		}	

	    }        
	}
    
    int i;
    puts("saiu do loop");
    for(i = 0; i < clientsSize-1; i++){
        setbuf(stdout, NULL);
        printf("joining with thread %d\n", i);
        pthread_join(clients[i], NULL);
        printf("joined with thread %d\n", i);
    }

    //fecha balcao, fecha loja caso seja o ultimo balcao a fechar e apaga os fifos do balcao
	encerraBalcao(mem, currentBalcao, sem_id);
	encerraLoja(mem, sem_id, argv[1], currentBalcao);
   sem_close(sem_id);
   unlink(fifoName);
   if (munmap(mem, sizeof(mem_part)) == -1)
       perror("balcao: error, couldn't unmap shared memory:");

	exit(0);
}
