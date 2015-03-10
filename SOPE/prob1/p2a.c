#include <stdio.h>
#include <stdlib.h>

void greet(char* str)
{
   printf("Hello %s!\n", str);
}

int main(int argc, char** argv)
{
   if (argc == 2)
      greet(argv[1]);
   else if (argc == 3)
   {
      int repeat;
      for (repeat = atoi(argv[2]); repeat > 0; repeat--)
       greet(argv[1]);      
   }
   else
      return 1;
   return 0;
}
