#!/bin/bash



for ARG in "$@"
   do
      rm -f $ARG
      cc -Wall $ARG".c" -o $ARG
      if [ $? = 0 ]; then 
         echo "Compiled $ARG"
      else
         echo "Couldn't compile $ARG"
      fi        
   done



