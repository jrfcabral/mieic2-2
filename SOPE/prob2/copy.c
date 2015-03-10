#include <stdio.h>
#include <termios.h>
#include <unistd.h>
#include <stdlib.h>


int main()
{
   struct termios oldterm, newterm;
   tcgetattr(STDIN_FILENO, &oldterm);
   newterm = oldterm;
   newterm.c_lflag &= ~(ECHO | ECHOE | ECHOK | ECHONL | ICANON);
   tcsetattr(STDIN_FILENO, TCSAFLUSH, &newterm);
   
   char mark = '*';
   
   void* input = malloc(1);
   int size = 1;
   
   while (read(STDIN_FILENO, input+size-1, 1) && *((char*)(input+size-1)) != '\n'){
      write(STDOUT_FILENO, (void*)&mark, 1);
      input = realloc(input, ++size);
      }
   
   
   write(STDOUT_FILENO, input, size);
   tcsetattr(STDIN_FILENO, TCSANOW, &oldterm);
   
   return 1;
   
}
