#include <unistd.h>
#include <dirent.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <stdio.h>
#include <errno.h>
#include <limits.h>

/*Calls csc on the specified path as a child process*/
void call_csc(char* path){
	if (strcmp(path, "temp.txt") == 0){
		printf("ola\n");
		exit(-2);
	}
    int pid = fork();
    if (pid < 0){
        perror(strerror(errno));
        exit(errno);
    }
    else if (pid == 0){
        wait(&pid);
        if (pid)
            puts("csc returned an error code");
    }
    else if(execlp("./csc", "./csc", path, NULL)){
        perror(strerror(errno));
        exit(errno);
    }
}

/*Calls sw on the specified file in the specified directory as a child process*/
void call_sw(char *filename, char *dir){
	pid_t pid = fork();
	if(pid == 0){
		execlp("./sw", "./sw", filename, dir, NULL);
		printf("Erro...\n");
		exit(-1);
	}
	else if(pid < 0){
		printf("Erro...\n");
	}
	else{
		wait(NULL);
	}
}

/*Checks whether the specified directory holds the files necessary for index to run.*/
int checkFiles(char *dir){

	int hasWords = 0, hasFiles = 0;
	DIR *src;
	struct dirent *src_ent;
	struct stat ent_stat;
	
	src = opendir(dir);
	if(src == NULL){
		write(STDERR_FILENO, "Could not open directory.", 25);
		exit(-1);
	}

	while((src_ent = readdir(src)) != NULL){
		char *pathedFile = (char *)malloc(strlen(dir)+ strlen(src_ent->d_name)*sizeof(char)+2*sizeof(char));
		strcpy(pathedFile, dir);
		strcat(pathedFile, "/");
		strcat(pathedFile, src_ent->d_name);

		lstat(pathedFile, &ent_stat);		
		
		if(!strcmp(src_ent->d_name, "words.txt")){
			printf("Found words.txt\n");
			hasWords = 1;
		}
		else if(S_ISREG(ent_stat.st_mode)){
			printf("Found ");
			puts(src_ent->d_name);
			hasFiles = 1;
		}

		free(pathedFile);		
	
		if(hasWords && hasFiles){
			printf("Initiating search...\n");
			closedir(src);			
			return 1;
		}
	}
	closedir(src);
	return 0;
}


int main(int argc, char **argv){
	if(argc != 2){
		printf("\nUsage: %s dir\n\n", argv[0]);
		exit(-1);
	}
	
	char buf[PATH_MAX+1];
    	char *real = realpath(argv[1], buf);
	if(real == NULL){
		printf("Error. The specified directory is probably non-existant.\n");
		exit(-1);
	}

	if(!checkFiles(real)){
		printf("The program did not find the necessary files to proceed.\nMake sure to have a words.txt files aswell as other text files to be parsed.\n");
		exit(-1);	
	}
	
	DIR *src;
	struct dirent *src_ent;
	struct stat ent_stat;
	
	src = opendir(real);
	if(src == NULL){
		write(STDERR_FILENO, "Could not open directory.", 25);
		exit(-1);
	}	
	
	while((src_ent = readdir(src)) != NULL){
		char *pathedFile = (char *)malloc(strlen(real)+ strlen(src_ent->d_name)*sizeof(char));
		strcpy(pathedFile, real);
		strcat(pathedFile, "/");
		strcat(pathedFile, src_ent->d_name);
		lstat(pathedFile, &ent_stat);

		if(!strncmp(src_ent->d_name, "words.txt", 8) || strncmp(src_ent->d_name, "res_", 4) == 0){
			continue;
		}
		else if(S_ISREG(ent_stat.st_mode)){
			call_sw(src_ent->d_name, real);		
		}
		else{
			free(pathedFile);		
		}
	}

	call_csc(real);
	return 0;	
}
