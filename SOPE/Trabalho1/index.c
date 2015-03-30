#include <unistd.h>
#include <dirent.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>

//A MERDA DO S_ISREG N DISTINGUIA UM FICHEIRO DUM DIRETORIO
//NEM Q O CABRAO O TIVESSE A VIOLAR COM UM GARFO
//Srsly tho eu tenho um programa em q uso S_ISREG e funciona bem (quase).
//Neste (cujo processo do S_ISREG foi copy-pasted desse) 
//so e ficheiro as terças, entre as 10 e as 16, excepto almoço


//N tou a usar system calls em tudo (ainda)
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
		lstat(src_ent->d_name, &ent_stat);
		
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
		
		if(!strcmp(src_ent->d_name, "words.txt")){
			continue;
		}
		else if(S_ISREG(ent_stat.st_mode)){
			puts("Launch sw...");
		}
	}

	return 0;	
}
