#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/times.h>
#include <unistd.h> 

int randnum(int higher)
{
   return rand() % higher;
}

int main(int argc, char** argv)
{
   if(argc != 3)
      exit(1);      
   
   srand(time(NULL));
   
   int limit, number;
   limit = atoi(argv[1]);
   number = atoi(argv[2]);
   
   if (limit <= 0)
      exit(2);
      
   int rand = -1;
   int i = 0;
   
   clock_t start, end;
   struct tms t;
   long ticks;

   start = times(&t); /* início da medição de tempo */
   ticks = sysconf(_SC_CLK_TCK); 
   
   
   for (; rand != number; i++)
   {     
      rand = randnum(limit);
     // printf("%d:%d\n", i, rand);
   }

   
   end = times(&t); 

    printf("Clock: %4.2f s\n", (double)(end-start)/ticks);
    printf("User time: %4.2f s\n", (double)t.tms_utime/ticks);
    printf("System time: %4.2f s\n", (double)t.tms_stime/ticks);
    printf("Children user time: %4.2f s\n", (double)t.tms_cutime/ticks);
    printf("Children system time: %4.2f s\n", (double)t.tms_cstime/ticks);
    printf("Numero de tentativas: %d\n", i);
   exit(0);
}

