#Makefile for SOPE project #2
#João Cabral & João Mota
CFLAGS = -Wall -g -O0 -lpthread -lrt
CC = gcc
OBJECTS_BALCAO = balcao.o loja.o
OBJECTS_GER_CLIENTE = cliente.o loja.o

SHELL := /bin/bash

all: balcao ger_clientes	

balcao: $(OBJECTS_BALCAO) 
	$(CC) $(OBJECTS_BALCAO) $(CFLAGS) -o bin/balcao 
ger_clientes: $(OBJECTS_GER_CLIENTE) 
	$(CC) $(OBJECTS_GER_CLIENTE) $(CFLAGS) -o bin/ger_cl  


clean:
	 @rm -f *.o bin/balcao bin/ger_cl /tmp/fd_* /tmp/fc_*



