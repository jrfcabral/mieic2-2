#Makefile for SOPE project #1
#João Cabral & João Mota
CFLAGS = -Wall
CC = gcc
OBJECTS_BALCAO = balcao.o
OBJECTS_GER_CLIENTE = ger_cliente.o
SHELL := /bin/bash

all: balcao 	

balcao: $(OBJECTS_BALCAO) 
	cc $(OBJECTS_BALCAO) -o bin/balcao -Wall -lpthread -lrt
index: $(OBJECTS_GER_CLIENTE) 
	cc $(OBJECTS_INDEX) -o bin/ger_cliente

clean:
	 @rm -f *.o balcao cliente /tmp/fd_*



