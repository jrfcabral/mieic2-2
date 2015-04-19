#Makefile for SOPE project #1
#João Cabral & João Mota
CFLAGS = -Wall
CC = gcc
OBJECTS_SW = sw.o
OBJECTS_INDEX = index.o
OBJECTS_CSC = csc.o
SHELL := /bin/bash

all: sw index csc	

sw: $(OBJECTS_SW) 
	cc $(OBJECTS_SW) -o bin/sw
index: $(OBJECTS_INDEX) 
	cc $(OBJECTS_INDEX) -o bin/index

csc: $(OBJECTS_CSC)
	cc $(OBJECTS_CSC) -o bin/csc

clean:
	 @rm -f *.o index sw csc res_* index.txt temp.txt



