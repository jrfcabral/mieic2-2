#include <stdlib.h>
#include <stdio.h>
#include <unistd.h> 

#define BUFFER_SIZE 10

int main(int argc, char** argv)
{
   if (argc != 3){
      printf("Usage: 2 file paths to copy from first to second\n");    
      exit(1);
   }
   
   int origin, target;

   origin = open(argv[1], O_RDONLY);
   target = open(argv[2], O_WRONLY | O_CREATE | O_TRUNC);
   
   if (target == NULL || origin == NULL){
      printf("Couldn't open files\n");
      exit(2);
   }
   
   void* buffer = malloc(BUFFER_SIZE);
   if (buffer == NULL)
      exit(4);
  
   
   int read_elem;
   while ( (read_elem = fread(buffer, 1, BUFFER_SIZE, origin)) ){
      fwrite(buffer, 1, read_elem, target);
   }
   
   fclose(origin);
   fclose(target);
   free(buffer);
   
   exit(0);
}
