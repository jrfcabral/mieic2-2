#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>



int main(int argc, char **argv){
	if(argc != 2){
		printf("Usage: %s file\n", argv[0]);
		exit(-1);	
	}
	
	FILE *words;
	words = fopen("words.txt", "r");
	if(words == NULL){
		perror(argv[1]);
		exit(-1);	
	}	
	
	char *name = (char *)malloc(strlen(argv[1])*sizeof(char));
	strcpy(name, argv[1]);
	int i;
	for(i = strlen(name); i > 0; i--){
		if(name[i] == '.'){
			name[i] = '\0';			
		}
	}	
	char *fpath = (char *)malloc((4+strlen(name)+4)*sizeof(char));
	strcpy(fpath, "res_");
	strcat(fpath, name);
	//puts(fpath);	
	strcat(fpath, ".txt");
	//puts(name);
	//puts(fpath);
		
	int search_res;

	search_res = open(fpath, (O_CREAT | O_WRONLY | O_TRUNC), 0777);
	if(search_res< 0){
		perror(argv[1]);
		exit(-1);	
	}
		

	pid_t pid;
	char *word = (char *)malloc(256*sizeof(char));
	int std = dup(STDOUT_FILENO);
	while(!feof(words)){
		int fd[2];
		pipe(fd);
		fgets(word, 256, words);
		word[strlen(word) - 1] = '\0';
		pid = fork();
		if(pid == 0){
			close(fd[0]);
			dup2(fd[1], STDOUT_FILENO);
			execlp("grep", "grep", "-no", word, argv[1], NULL);
			printf("Command not executed.\n");
			exit(-1);
		}
		else{
			close(fd[1]);
			int n;
			wait(&n);
			char result[10000];
			n = read(fd[0], result, 10000);
			fgets(result, 1000, );
			result[n] = '\0';
			puts(result);
			
		}		
		
	}
	
	dup2(std, STDOUT_FILENO);
	
}
