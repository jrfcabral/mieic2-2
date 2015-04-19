//SOPE-FEUP Project #1
//João Cabral & João Mota
//sw.c
//created 15th of April 2015
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>

char *buildWordsDir(char *dir){
	char *wordsDir = (char *)malloc((strlen(dir)+8)*sizeof(char)+3);
	strcpy(wordsDir, dir);
	strcat(wordsDir, "/");
	strcat(wordsDir, "words.txt");
	
	return wordsDir;
}

char *extractFileName(char *filename){
	char *name = (char *)malloc(strlen(filename)*sizeof(char)+2);
	strcpy(name, filename);
	char *dump = strtok(name, ".");
	return dump;
}

char* buildResString(char *filename){
	char *resString = (char *)malloc((4+strlen(filename)+4)*sizeof(char)+2);
	strcpy(resString, "res_");
	strcat(resString, filename);
	strcat(resString, ".txt");
	return resString;
}

char* buildGrepName(char *filename, char *dir){
	char *grepName = (char *)malloc((strlen(filename) + strlen(dir))*sizeof(char)+2);
	strcpy(grepName, dir);
	strcat(grepName, "/");
	strcat(grepName, filename);
	return grepName;
}


int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: %s file dir\n", argv[0]);
		exit(-1);	
	}
	//building string for words.txt
	char *wordsDir = buildWordsDir(argv[2]);

	//opening words.txt
	int words;
	words = open(wordsDir, O_RDONLY);
	if(words == -1){
		perror(wordsDir);
		exit(-1);	
	}
	//getting words from words.txt
	char *wordsContent = (char *)malloc(100*sizeof(char)+1);
	int n;
	while((n = read(words, wordsContent, 100)) > 0){
		wordsContent = (char *)realloc(wordsContent, 100+strlen(wordsContent)*sizeof(char));
	}
	close(words);
	
	
	//Extracting the file's name (without file extension)
	char *name = extractFileName(argv[1]);
	
	//Building res_ string for receiving file.
	char *fpath = buildResString(name);

	int search_res;
	//Opening/Creating receiver file
	search_res = open(fpath, (O_CREAT | O_WRONLY | O_TRUNC), 0777);
	if(search_res< 0){
		perror(argv[1]);
		exit(-1);	
	}
		
	//Building string for grep search	
	char *grepName = buildGrepName(argv[1], argv[2]);

	pid_t pid;
	char *word, *save_wordptr, *save_dumpptr;
	int i = 0;
	int std = dup(STDOUT_FILENO); //Store stdout in a safe location
	while(1){ //grep cycle
		int fd[2];
		pipe(fd);

		if(i++ == 0){ 
			word = strtok_r(wordsContent, "\n", &save_wordptr);
			if(word == NULL)
				break;
		}
		else{
			word = strtok_r(NULL, "\n", &save_wordptr);
			if(word == NULL)
				break;
		}
		
		pid = fork();
		if(pid == 0){
			close(fd[0]);
			dup2(fd[1], STDOUT_FILENO);
			execlp("grep", "grep", "-now", word, grepName, NULL);
			printf("Command not executed.\n");
			exit(-1); 
		}
		else{
			close(fd[1]);
			int n;
			wait(&n);
			char result[10000];
			char *dump;
			n = read(fd[0], result, 10000);
			result[n] = '\0';
			
			dump = strtok_r(result, ":\n", &save_dumpptr);
			if(dump != NULL){
				while(1){
					write(search_res, word, strlen(word));
					write(search_res, ": ", 2);
					write(search_res, name, strlen(name));
					write(search_res, "-", 1);
					write(search_res, dump, strlen(dump));
					write(search_res, "\n", 1);
					if((dump = strtok_r(NULL, ":\n", &save_dumpptr)) == NULL)
						break;
					if((dump = strtok_r(NULL, ":\n", &save_dumpptr)) == NULL)
						break;
				}
			}
			
		}	
		
	}
	
	dup2(std, STDOUT_FILENO);
	close(search_res);	
	return 0;
}
