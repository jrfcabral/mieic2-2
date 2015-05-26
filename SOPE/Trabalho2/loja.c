#include "loja.h"

void printLog(char* name, char* who, int number, char* what, char* channel, mutex_t *mut){
	time_t now = time(NULL);
	struct tm time;
	localtime_r(&now, &time);
	//calculate the length of the line to be printed
	int len = snprintf(NULL ,0, "%4d-%2d-%2d %2d:%2d:%2d |%-10s|%6d|%-20s|%-30s\n", time.tm_year+1900,time.tm_mon,time.tm_mday,
	time.tm_hour,time.tm_min, time.tm_sec, who,number, what, channel);	
	
	//try to create file, open if it already exists
	pthread_mutex_lock(mut);
	char* filename = malloc(sizeof(char)*(strlen(".log")+strlen(name)+1));
	sprintf(filename, "%s%s", name, ".log");
	int fd = open(filename, (O_CREAT|O_EXCL|O_WRONLY), 0777);

	
	if (fd > 0){
		char* firstLine = malloc(sizeof(char)*(len+2));
		snprintf(firstLine, len+1, "%-20s|%-10s|%-6s|%-20s|%-30s\n", "quando", "quem", "balcao", "o_que", "canal criado/usado");		
		write(fd, firstLine, strlen(firstLine));
		free(firstLine);
	}
	if (fd < 0 && errno == EEXIST)
		fd = open(filename, O_WRONLY |O_APPEND, 0777);
	if (fd < 0){
		free(filename);
		pthread_mutex_unlock(mut);
		perror("loja.c:Couldn't open log file");
		return;
	} 
	
	free(filename);
	char* line = malloc(sizeof(char)*(len+1));
	snprintf(line ,len+1, "%04d-%02d-%02d %02d:%02d:%02d |%-10s|%-6d|%-20s|%-30s\n", time.tm_year+1900,time.tm_mon,time.tm_mday,
	time.tm_hour,time.tm_min, time.tm_sec, who,number, what, channel);
	write(fd, line, len);
	close(fd);
	pthread_mutex_unlock(mut);
	free(line);
	return;
}
