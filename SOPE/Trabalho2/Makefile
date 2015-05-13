#Makefile for SOPE project #2
#João Cabral & João Mota
CFLAGS = -Wall
CC = gcc
OBJECTS_BALCAO = balcao.o
OBJECTS_GER_CLIENTE = cliente.o
SHELL := /bin/bash

all: balcao ger_clientes	

balcao: $(OBJECTS_BALCAO) 
	cc $(OBJECTS_BALCAO) -o bin/balcao -Wall -lpthread -lrt
ger_clientes: $(OBJECTS_GER_CLIENTE) 
	cc $(OBJECTS_GER_CLIENTE) -o bin/ger_cl -lpthread -lrt 

clean:
	 @rm -f *.o bin/balcao bin/ger_cl /tmp/fd_* /tmp/fc_*



