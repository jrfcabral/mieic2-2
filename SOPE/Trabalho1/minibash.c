#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>

int main(){
	char *toks[10];
	char comm[256], *dump;
	int fd_dest = -1;
	while(1){
		int n;
		write(STDOUT_FILENO, "minish >", 8);
		n = read(STDIN_FILENO, &comm, 255);
		comm[n-1] = '\0';
		if(!strncmp(comm, "quit", 4)){
			break;		
		}

		dump = strtok(comm, " ");
		if(dump == NULL)
			continue;
		toks[0] = (char *)malloc(strlen(dump)*sizeof(char));
		strcpy(toks[0], dump);
		
		int i;
		for(i = 1; i < 10; i++){
			dump = strtok(NULL, " ");
			if(dump == NULL){
				toks[i] = NULL;
			}
			else{
				toks[i] = (char *)malloc(strlen(dump)*sizeof(char));
				strcpy(toks[i], dump);
			}	
		}	
			
		for(i = 2; i < 10; i++){
			if(toks[i] == NULL){
				if(!strcmp(toks[i-2], "-o")){
					fd_dest = open(toks[i-1], (O_CREAT | O_WRONLY | O_TRUNC), 0777);
					toks[i-2] = NULL;
					toks[i-1] = NULL;
					break;
				}
				else break;
			}
		}	

		int std = dup(STDOUT_FILENO);
		
		pid_t pid = fork();
		if(pid == 0){
			if(fd_dest != -1){	
				dup2(fd_dest, STDOUT_FILENO);
			}
			

			execlp(toks[0], toks[0], toks[1], toks[2], toks[3], toks[4], toks[5], toks[6], toks[7], toks[8], toks[9], NULL);
			write(STDERR_FILENO, "Command failed\n", 15);
			exit(-1);
			
		}
		else if(pid > 0){
			wait(&n);
			if(fd_dest != -1){
				fd_dest = -1;
				dup2(std, STDOUT_FILENO);
			}
		}
		
	}

}	
	
