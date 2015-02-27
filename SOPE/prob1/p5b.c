#include <stdio.h>
#include <stdlib.h>
#include <string.h>


void greet(char* str)
{
   printf("Hello %s!\n", str);
}

int main(int argc, char** argv, char** envp)
{
   if (argc == 2)
      greet(argv[1]);
   else if (argc == 3)
   {
      int repeat;
      for (repeat = atoi(argv[2]); repeat > 0; repeat--)
       greet(argv[1]);      
   }
   else if (argc == 1)
   {      
      char* user = getenv("USER");
      if (user == NULL)
         perror("No user!\n");
      else   
         greet(user);       
      
   }
   else
      return 1;
   return 0;
}
