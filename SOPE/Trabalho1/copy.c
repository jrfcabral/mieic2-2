#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/times.h> //for time measurement

int main(int argc, char **argv){
	if(argc != 3){
		printf("Usage: copy dest src\n");
		exit(-1);	
	}
	
	struct tms time;
	
	clock_t begin = times(&time), end;
	void *byte;
	FILE *dest, *src;
	dest = fopen(argv[1], "wb");
	if(dest == NULL){
		printf("Could not create file or open destination file.\n");
		exit(-1);	
	}
	src = fopen(argv[2], "rb");
	if(src == NULL){
		printf("Could not find or open source file.\n");
exit(-1);
	}

	while(!feof(src)){
		fread(byte, sizeof(char), 1, src);
		fwrite(byte, sizeof(char), 1, dest);
	}

	fclose(dest);
	fclose(src);
	
	end = times(&time);
	printf("%f seconds elapsed\n", (float)(end-begin)/sysconf(_SC_CLK_TCK));
}
