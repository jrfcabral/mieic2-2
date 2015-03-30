#include <unistd.h>
#include <dirent.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>

//Isto com fread e fwrite era um tirinho...
//Ignores files randomly...
int main(int argc, char **argv){

	if(argc != 3){
		write(STDERR_FILENO, "Usage: rcopy dest src\n", 25);
		exit(-1);	
	}
	
	DIR *dest, *src;
	struct dirent *src_ent;
	struct stat ent_stat;
	pid_t pid;
	
	dest = opendir(argv[1]);
	if(dest == NULL){
		write(STDERR_FILENO, "Could not open directory.", 25);
		exit(-1);
	}
	
	src = opendir(argv[2]);
	if(src == NULL){
		write(STDERR_FILENO, "Could not open directory.", 25);
		exit(-1);
	}

	while((src_ent = readdir(src)) != NULL){
		lstat(src_ent->d_name, &ent_stat);
		if(S_ISREG(ent_stat.st_mode)){
			pid = fork();
		}

		if(pid == 0){
			char *path_src, *path_dest;
			path_src = (char *)malloc(265*sizeof(char));
			path_dest = (char *)malloc(265*sizeof(char));
			strcpy(path_src, argv[2]);
			strcpy(path_dest, argv[1]);
			strcat(path_src, "/");
			strcat(path_dest, "/");
			strcat(path_src, src_ent->d_name);
			strcat(path_dest, src_ent->d_name);
			
			void *byte;
			int fd_dest, fd_src;
	
			fd_dest = open(path_dest, (O_CREAT | O_WRONLY | O_TRUNC), 0777); //Without O_TRUNC, vestiges of what was in the file before might be left
			fd_src = open(path_src, O_RDONLY);
			while(read(fd_src, &byte, 1)){
				write(fd_dest, &byte, 1);
			}

			close(fd_dest);
			close(fd_src);
			
			exit(0);	
		}
		
	}
}
