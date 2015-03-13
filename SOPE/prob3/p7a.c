// PROGRAMA p7.c
#include <unistd.h>
int main(int argc, char *argv[]) 
{
   char prog[20];
   sprintf(prog,"%s.c",argv[1]);
   execlp("gcc","gcc",prog,"-Wall","-o",argv[1],NULL);
   printf("%s", "Se aqui chegasteis é porque falhou\n");
   exit(1);
} 
