// PROGRAMA p01a.c
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
void sigint_handler(int signo)
{
    printf("In SIGINT handler (mas vou continuar a dormir) ...\n");
}
int main(void)
{
    if (signal(SIGINT,sigint_handler) < 0)
    {
       fprintf(stderr,"Unable to install SIGINT handler\n");
       exit(1);
    }
    printf("Sleeping for 30 seconds ...\n");
    int sono = 30;
    while ((sono = sleep(sono)) != 0);
    printf("Waking up ...\n");
    exit(0);
}
