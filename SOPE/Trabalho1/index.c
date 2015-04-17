#include <unistd.h>
#include <dirent.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <stdio.h>
#include <errno.h>
void call_csc(char* path){
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

void call_sw(char *filename, char *dir){
	pid_t pid = fork();
	if(pid == 0){
		execlp("./sw", "./sw", filename, dir, NULL);
		printf("Erro...\n");
	}
	else if(pid < 0){
		printf("Erro...\n");
	}
}

int main(int argc, char **argv){
	if(argc != 2){
		printf("\nUsage: %s dir\n\n", argv[0]);
		exit(-1);
	}
	
	int hasWords = 0, hasFiles = 0;
	DIR *src;
	struct dirent *src_ent;
	struct stat ent_stat;
	
	src = opendir(argv[1]);
	if(src == NULL){
		write(STDERR_FILENO, "Could not open directory.", 25);
		exit(-1);
	}

	while((src_ent = readdir(src)) != NULL){
		char *test = (char *)malloc(strlen(argv[1])+ strlen(src_ent->d_name)*sizeof(char));
		strcpy(test, argv[1]);
		strcat(test, src_ent->d_name);
		lstat(test, &ent_stat);		
		
		if(!strcmp(src_ent->d_name, "words.txt")){
			printf("Found words.txt\n");
			hasWords = 1;
		}
		else if(S_ISREG(ent_stat.st_mode)){
			printf("Found ");
			puts(src_ent->d_name);
			hasFiles = 1;
		}
		if(hasWords && hasFiles){
			printf("Initiating search...\n");
			break;
		}	
	}
	if(!(hasWords && hasFiles)){
		printf("The program did not find the necessary files to proceed.\nMake sure to have a words.txt files aswell as other text files to be parsed.\n");
		exit(-1);	
	}
	closedir(src);

	src = opendir(argv[1]);
	if(src == NULL){
		write(STDERR_FILENO, "Could not open directory.", 25);
		exit(-1);
	}	
	
	while((src_ent = readdir(src)) != NULL){
		lstat(src_ent->d_name, &ent_stat);
		
		if(!strcmp(src_ent->d_name, "words.txt") || strncmp(src_ent->d_name, "res_", 4) == 0){
			continue;
		}
		else if(S_ISREG(ent_stat.st_mode)){
			/*pid_t pid = fork();
			if(pid == 0){
				execlp("./sw", "./sw", src_ent->d_name, NULL);
				printf("Erro...\n");
			}*/
			call_sw(src_ent->d_name, argv[1]);		
		}
	}
	

	call_csc(argv[1]);

	return 0;	
}
