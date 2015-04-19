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
#include <libgen.h>
/*Calls csc on the specified path as a child process*/
void call_csc(char* path){
	if (strcmp(path, "temp.txt") == 0){
		exit(-2);
	}
    char fullpath[PATH_MAX+1];
    readlink("/proc/self/exe", fullpath, PATH_MAX+1);
    char* exepath;
    exepath = dirname(fullpath);
    strcat(exepath, "/csc");	
	puts(exepath);	
    int pid = fork();
    if (pid < 0){
        perror(strerror(errno));
        exit(errno);
    }
    else if (pid){
	int res;
        waitpid(pid, &res, 0);
    }
    else if(execlp(exepath, exepath, path, NULL)){
        perror(strerror(errno));
        exit(errno);
    }
}

/*Calls sw on the specified file in the specified directory as a child process*/
void call_sw(char *filename, char *dir){
	char path[PATH_MAX+1];
	readlink("/proc/self/exe", path, PATH_MAX+1);
	char* exepath;
	exepath = dirname(path);
	strcat(exepath, "/sw");	
	puts(exepath);
	pid_t pid = fork();
	if(pid == 0){
		execlp(exepath, exepath, filename, dir, NULL);
		printf("Erro...\n");
		exit(-1);
	}
	else if(pid < 0){
		printf("Erro...\n");
	}
	else{
		waitpid(pid,NULL,0);
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
			hasWords = 1;
		}
		else if(S_ISREG(ent_stat.st_mode)){
			hasFiles = 1;
		}

		free(pathedFile);		
	
		if(hasWords && hasFiles){
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

		if(!strncmp(src_ent->d_name, "words.txt", 8) || strncmp(src_ent->d_name, "res_", 4) == 0 || !strncmp(src_ent->d_name, "index.txt", 9)){
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
