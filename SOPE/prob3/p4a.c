#include <unistd.h>
#include <sys/wait.h>

int main()
{
   if (fork() != 0){
      wait(0);
      write(STDOUT_FILENO, " world!", 7);
   }
   else{
      if (fork() != 0){
         wait(0);
         write(STDOUT_FILENO, " oh my", 6);
      }
      else
         write(STDOUT_FILENO, "Hello", 5);
   }
      
  return 0;
}
