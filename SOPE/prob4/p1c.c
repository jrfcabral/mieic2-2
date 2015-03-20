// PROGRAMA p01a.c
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
void sigint_handler(int signo)
{
    printf("In SIGINT handler (mas vou continuar a dormir) ...\n");
}

void sigint_other_handler(int signo)
{
   printf("SIGINT HANDLER - signo = %d \n", signo);
}
int main(void)
{
   struct sigaction old_sigaction;
   sigaction(SIGINT, NULL, &old_sigaction);
   old_sigaction.sa_handler = sigint_other_handler;
   sigaction(SIGINT, &old_sigaction, NULL);
    
     
    printf("Sleeping for 30 seconds ...\n");
    int sono = 30;
    while ( ( sono = sleep(sono) ) != 0 );
    printf("Waking up ...\n");
    exit(0);
}
