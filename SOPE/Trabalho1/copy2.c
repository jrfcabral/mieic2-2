#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/times.h> //for time measurement

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: copy dest src\n");
		exit(-1);	
	}
	
	struct tms time;

	clock_t begin = times(&time), end;	

	void *byte;
	int fd_dest, fd_src;
	
	fd_dest = open(argv[1], (O_CREAT | O_WRONLY | O_TRUNC), 0777); //Without O_TRUNC, vestiges of what was in the file before might be left
	fd_src = open(argv[2], O_RDONLY);
	while(read(fd_src, &byte, 1)){
		write(fd_dest, &byte, 1);
	}

	close(fd_dest);
	close(fd_src);
	
	end = times(&time);
	
	printf("%f seconds elapsed\n", (float)(end-begin)/sysconf(_SC_CLK_TCK));
	
}
