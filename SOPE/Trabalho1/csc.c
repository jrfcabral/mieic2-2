//SOPE-FEUP Project #1
//João Cabral & João Mota
//csc.c
//created 15th of April 2015
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <limits.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <dirent.h>
#include <errno.h>
#include <string.h>

//calls cat with the arguments provided by args, outputs to fd
void concatenate(char** args, int fd){
    int backup = dup(STDOUT_FILENO);
    dup2(fd, STDOUT_FILENO);    
    int pid = fork();
    if (pid < 0){
        perror(strerror(errno));
        exit(errno);
    }
    else if (pid){
        wait(&pid);
        if (pid)
            puts("cat returned an error code");
    }
    else if(execvp(*args, args) < 0){
        perror(strerror(errno));
        exit(errno);
    }  
        
    dup2(backup, STDOUT_FILENO);   
}

//calls sort on the file provided by path, outputs to fd
void sort(char* path, char* outpath){
    int pid = fork();
    int res;
    if (pid < 0){
        perror(strerror(errno));
        exit(errno);
    }
    else if (pid){
        wait(&res);
        if (res)
            puts("sort returned an error code");
    }
    else if(execlp("sort", "sort", path, "-o", path, NULL) < 0){
        perror(strerror(errno));
        exit(errno);
    }
}

//joins lines that have the same first word, inputs from inpath, outputs to outpath
void clean(char* inpath, char* outpath){    
	if(!strcmp(inpath, outpath)){
        perror("clean: cannot output to the file being read\n");
        exit(-1);
    }
    
    //redirect input and output
    int backup_in = dup(STDIN_FILENO);
    int backup_out = dup(STDOUT_FILENO);
    int in = open(inpath, (O_RDONLY), 0777);    
    int out = open(outpath, (O_CREAT | O_TRUNC | O_WRONLY), 0777);
    dup2(in, STDIN_FILENO);
    dup2(out, STDOUT_FILENO);
    if (backup_in < 0 || backup_out < 0 || in < 0 || out < 0){
        perror("clean: couldn't redirect output!");
        exit(-1);
    }
    
    //call awk
    int pid = fork();
    if (pid < 0){
        perror(strerror(errno));
        exit(errno);
    }
    else if (pid){
       wait(&pid);
       if (pid)
           puts("awk returned an error code");
    }
    else if (execlp("awk", "awk", "{line=\"\";for(i=2;i <= NF; i++)line = line ($i (i == NF ?\", \" : \" \")); table[$1] = table[$1] line;} END {for (key in table) print key \" \" substr(table[key],0, length(table[key])-2)	;}", NULL) < 0){
        perror(strerror(errno));
        exit(errno);
    }    
    //restore output/input
    dup2(backup_in, STDIN_FILENO);
    dup2(backup_out, STDOUT_FILENO);
    close(in);
    close(out);
}

int main(int argc, char** argv)
{
    //try to open current directory and fail on error
    char currentdir[1024];
    if (argc == 1 || !strcmp(argv[1],"."))
        getcwd(currentdir, 1024);
    else if (argc == 2)
        strcpy(currentdir, argv[1]);
    else
        printf("Usage: csc directory\n");
    char buf[PATH_MAX+1];
    char* real = realpath(currentdir, buf);
   // printf("opening %s directory\n", real);
	DIR* dir = opendir(".");
   	if (dir == NULL){
	    perror(strerror(errno));
	    exit(errno);
	}  

    //iterate over the directory entries to find the ones that begin with res_ and save them to a vector of strings
    struct dirent *entry;
    char prefix[] = "res_";
    char **found = malloc(sizeof(char*));
    found[0] = malloc(sizeof(char)*strlen("cat"));
    strcpy(found[0], "cat");    
    int foundSize = 1;
    for(entry = readdir(dir);entry != NULL; entry = readdir(dir))
        if (strncmp(prefix, entry->d_name, 4) == 0){
            found = realloc(found, ++foundSize*sizeof(char*));  
            found[foundSize-1] = malloc(sizeof(char)*strlen(entry->d_name));
            strcpy(found[foundSize-1], entry->d_name);            
        }
    
    //two things can cause readdir after iterate to return NULL - an error or no more files to read so check for error after NULL
    if (errno){
        perror("error with readdir");
        exit(errno);
    }
    
    //open output file
    int temp = open("temp.txt", (O_CREAT | O_TRUNC | O_WRONLY), 0777);   
    if (temp < 0){
        close(temp);
        perror(strerror(errno));
        exit(-1);
    }
    //concatenate the files
    if (foundSize > 1){ //if there is anything to do       
        found = realloc(found, ++foundSize*sizeof(char*));
        found[foundSize-1] = NULL; //arg array needs to end in null for execvp
        concatenate(found, temp);       
    }
    else{
        close(temp);
        exit(0);//no files to index, exit successfully
    }
    
    //sort the concatenated file   
    sort("temp.txt",  "temp.txt");
	
    //join lines started by the same word    
    clean("temp.txt", strcat(real,"/index.txt"));
    close(temp);
    if(unlink("temp.txt"))
        perror("Couldn't delete temporary file");


    
    //release allocated memory
    for(;foundSize>0;foundSize--)free(found[foundSize-1]);
    free(found);
    exit(0);//successful run
}
