#!/bin/bash

echo "# Lauf mit $1 Threads " >  result.txt

for i in $(seq 1 $1)
do
  echo Lauf mit Threads: $i 
  erg=`java BooleanThread $i | grep Rechenzeit:`
  erg1=${erg:12}
  echo "$i  ${erg1} " >> result.txt
done

FILE=result.txt gnuplot x.plt -