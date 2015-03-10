#include <stdio.h>
#include <stdlib.h>
void msg1(void)
{
   printf("mensagem 1\n");

}
void msg2(void)
{
   printf("mensagem 2\n");

}

int main()
{
   atexit(msg1);
   atexit(msg1);

   printf("main done! \n");
   return 0;
}
