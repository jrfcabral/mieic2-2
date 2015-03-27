#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdlib.h>

#define MAXLINE 4096
#define READ   0
#define WRITE  1

int main()
{
   int n[2], fd[2];
   pid_t pid;
   char  line[MAXLINE];
   
   if (pipe(fd) < 0){
      fprintf(stderr, "Erro na pipe\n");
      exit(1);
   }
   
   if ( (pid = fork()) < 0){
      fprintf(stderr, "Erro no fork\n");
      exit(2);
   }
   
   if (pid > 0) {
      close(fd[READ]);
      int h =read(STDIN_FILENO, line, MAXLINE);
      write(fd[WRITE], line, h);
      h = read(STDIN_FILENO, line, MAXLINE);
      write(fd[WRITE], line, h);     
      
      close(fd[WRITE]);      
   }
   else{
      close(fd[WRITE]);
      int j =  read(fd[READ], line, MAXLINE);
      n[0] = atoi(line);
      j =  read(fd[READ], line, MAXLINE);
      n[1] = atoi(line);
      int res = n[0] * n[1];
      printf("%d\n", res);
      close(fd[READ]);
   }
   
   exit(0);   
   
   
}
