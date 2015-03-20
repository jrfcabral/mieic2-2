//Programa p3a.c (SOPE)
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
int sentido = 1;

void signal_handler(int signo)
{
   if (signo == SIGUSR1)
   sentido = 1;
   else if (signo == SIGUSR2)
   sentido = -1;
}


int main()
{
   int v = 0;
   struct sigaction action;
   action.sa_handler=signal_handler;
   sigaction(SIGUSR1, &action, NULL);
   sigaction(SIGUSR2, &action, NULL);
   
   while(1){
      v += sentido;
      printf("%d\n", v);
      sleep(1);
   }

   
}
