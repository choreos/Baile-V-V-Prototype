#!/bin/bash

MY_PROMPT='baile$ '

while :
do
  echo ""
  echo -ne "\033[1m$MY_PROMPT\033[0m"
  read line
  
  if [ $line == "start_chore" ]; then
	./scripts/setUp.sh
  
  elif [ $line == "exit" ]; then
	./scripts/stopChor.sh
	exit 0

  elif [ $line == "run_unit-tests" ]; then
   	ant runUnitTests
	echo ""
	echo -e "\033[1mtest reports saved in ./test-reports/unit dir....\033[0m"

  elif [ $line == "run_integration-tests" ]; then
	ant runIntegrationTests
	echo ""
	echo -e "\033[1mtest reports saved in ./test-reports/integration dir....\033[0m"

  elif [ $line == "run_acceptance-tests" ]; then
	ant runAcceptanceTests
	echo ""
	echo -e "\033[1mtest reports saved in ./test-reports/acceptance dir....\033[0m"

  elif [ $line == "run_all-test" ]; then
 	ant runAllTests
	echo ""
 	echo -e "\033[1mtest reports saved in test-reports/all dir....\033[0m"
  
  else
	echo "invalid command"
  fi
  done

exit 0

