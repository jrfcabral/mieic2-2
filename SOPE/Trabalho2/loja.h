#define MAX_LINES 100
#define SEM_NAME "/semBalcao"
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