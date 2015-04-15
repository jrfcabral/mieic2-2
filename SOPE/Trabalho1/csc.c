//SOPE-FEUP Project #1
//João Cabral & João Mota
//csc.c
//created 15th of April 2015
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>
#include <string.h>

int main()
{
    //try to open current directory and fail on error
    char currentdir[1024];
    getcwd(currentdir, 1024);
	DIR* dir = opendir(currentdir);
   	if (dir == NULL){
	    perror(strerror(errno));
	    exit(errno);
	}  

    //iterate over the directory entries to find the ones that begin with res_
    struct dirent *entry;
    char prefix[] = "res_";
    char **found = malloc(sizeof(char*));
    for(entry = readdir(dir);entry != NULL; entry = readdir(dir)){
        if (strncmp(prefix, entry->d_name, 4) == 0)
            printf("%s", entry->d_name);
    }
    //two things can cause readdir to return NULL - an error or no more files to read
    if (errno){
        perror(strerror(errno));
        exit(errno);
    }
}
