#!/bin/bash
MY_PROMPT='baile$ '
while :
do
  echo -n "$MY_PROMPT"
  read line
  
  if [ $line == "run" ]; then
	./scripts/setUp.sh
  
  elif [ $line == "exit" ]; then
	exit 0
  
  else
	echo "invalid command"
  fi
  done

exit 0

# This example script, and much of the above explanation supplied by
# Stéphane Chazelas (thanks again).
