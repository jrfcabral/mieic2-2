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
#include <sys/file.h>

#define MAX_LINES 100
#define CLIENTE_TIMEOUT 11

typedef pthread_mutex_t mutex_t;

typedef struct _table{
	int balcao;
	int encerrado;
	time_t tempo;
	int duracao;
	char nome_fifo[15];
	int em_atendimento;
	int ja_atendidos;
	float tempo_med_atend;
	mutex_t mutex;
}table;

typedef struct _mem_part{
	time_t data_abert_loja;
	int nBalcoes;
	int balcoesDisponiveis;
	char nome_sem[10];
    table tabelas[MAX_LINES];
	char nome_mem[100];	
	mutex_t logmutex;
}mem_part;

typedef struct _infoAtendimento{
    char fifoName[100];
    int balcaoNumber;
    mem_part* mem;
}infoAtendimento;

typedef struct _infoAlarme{
	int tempo;
	int balcaoNumber;	
} infoAlarme;

typedef struct _mensagemBalcao{
	char fifoName[100];
	int close;
} mensagemBalcao;

void printLog(char* name, char* who, int number, char* what, char* channel, mutex_t *mutex);
