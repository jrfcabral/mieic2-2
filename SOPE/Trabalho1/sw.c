#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>

//Cagando nas system calls...

int main(int argc, char **argv){
	if(argc != 2){
		printf("Usage: %s file\n", argv[0]);
		exit(-1);	
	}
		
	
	char *fpath = "res_";
	strcat(fpath, argv[1]);
		
		
	int words, file, search_res;
	words = open("words.txt", O_RDONLY);
	if(words < 0){
		perror(argv[1]);
		exit(-1);	
	}
	
	puts("!!!");
	
	file = open(argv[1], O_RDONLY);
	if(file < 0){
		perror(argv[1]);
		exit(-1);	
	}

	puts("!!!");	
	
	search_res = open(fpath, (O_CREAT | O_WRONLY | O_TRUNC), 0777);
	if(search_res< 0){
		perror(argv[1]);
		exit(-1);	
	}

		

	pid_t pid;
	char *word;
	int std = dup(STDOUT_FILENO), n_read;
	dup2(search_res, STDOUT_FILENO);
	
	while((n_read = read(words, word, 256))){
		//pid = fork();
		puts(word);
		/*if(pid == 0){
			execlp("grep", "grep", "-n", word, argv[1], NULL);
			printf("Command not executed.\n");
			exit(-1);
		}
		else{
			int n;
			wait(&n);
		}		
		dup2(std, STDOUT_FILENO);*/
	}
	

	
}
