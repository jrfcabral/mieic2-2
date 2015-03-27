#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdlib.h>

#define MAXLINE 4096
#define READ   0
#define WRITE  1
#define RESPONSE_FLOAT  0
#define RESPONSE_INT    1
#define RESPONSE_ERR    -1

typedef struct{
int n1, n2;
} numbers;

typedef struct{
void* answer; int type;
} response;


int main()
{
   int fd[2], rd[2];
   pid_t pid;
   char  line[MAXLINE];
   
   if (pipe(fd) < 0 || pipe(rd) < 0){
      fprintf(stderr, "Erro nos pipes\n");
      exit(1);
   }
   
   if ( (pid = fork()) < 0){
      fprintf(stderr, "Erro no fork\n");
      exit(2);
   }
   
   if (pid > 0) {
      close(fd[READ]);
      close(rd[WRITE]);
      numbers* num = malloc(sizeof(numbers));
      response* res = malloc(sizeof(response));
      read(STDIN_FILENO, line, MAXLINE);
      num->n1 = atoi(line);
      read(STDIN_FILENO, line, MAXLINE);
      num->n2 = atoi(line);
      write(fd[WRITE], num, sizeof(numbers));
      read(rd[READ], res, sizeof(response));
      
      switch(res->type){
      case RESPONSE_ERR:
         printf("nao funcionou\n");
         break;
      case RESPONSE_INT:
         printf("deu um int %d\n", (*(int*)res->answer) );
         break;
      case RESPONSE_FLOAT:
         printf("deu um float %f\n", (*(double*)res->answer));
         break;         
      default:
         break;
      }
      
   }
   else{
      close(fd[WRITE]);
      close(rd[READ]);
      
      numbers* num = malloc(sizeof(numbers));
      read(fd[READ], num, sizeof(numbers));
      printf("%d-%d\n", num->n1, num->n2);
      response res;
      
      if (num->n2 == 0){
      res.type = RESPONSE_ERR;
      res.answer = NULL;
      }  
      
      else if (num->n1%num->n2 == 0){
         res.type = RESPONSE_INT;
         int result = num->n1/num->n2;
         res.answer = &result;
         
      }
      else{
         res.type = RESPONSE_FLOAT;
         double result = (float)num->n1/(float)num->n2;
         res.answer = &result;
      }
      
      write(rd[WRITE], &res, sizeof(response));
      
      close(fd[READ]);
      close(rd[WRITE]);
   }
   
   exit(0);   
   
   
}
