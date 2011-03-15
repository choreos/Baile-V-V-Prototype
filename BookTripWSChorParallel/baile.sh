#!/bin/bash

MY_PROMPT='baile$ ';
CHOR_STARTED=0;

help_message() {
	echo ""
	echo "Commands: "
	echo -e "\033[1mstart_chore\033[0m - start the choreography"
	echo -e "\033[1mstop_chore\033[0m - stop the choreography"
	echo -e "\033[1mrun_unit-tests\033[0m - run the unit tests"
	echo -e "\033[1mrun_integration-tests\033[0m run the integration tests"
	echo -e "\033[1mrun_acceptance-tests\033[0m - run the acceptance tests"
	echo -e "\033[1mrun_all-tests\033[0m - run all tests"
	echo -e "\033[1mexit\033[0m - stop the choreography if it is running and quit"
	echo -e "\033[1mhelp\033[0m - show this message"
	echo  ""
	echo "Contacts:"
	echo "Felipe Besson <besson@ime.usp.br>"
	echo "Pedro Leal <pedrombl@ime.usp.br>"
}
echo ""
echo -e "\E[1;34mBaile V&V - dez/2010"; tput sgr0
help_message

while :
do
  echo ""
  echo -ne "\033[1m$MY_PROMPT\033[0m"
  read line
  
  if [ $line == "start_chore" ]; then
	if [ $CHOR_STARTED -eq 1 ]; then
		echo -e '\E[1;31mcoreography has already been started ...'; tput sgr0
	else
		./scripts/setUp.sh
		result=$(grep "started" log.txt)
		if [ -n "$result" ]; then	
			CHOR_STARTED=1;
		fi
		rm log.txt
	fi

  elif [ $line == "stop_chore" ]; then
	if [ $CHOR_STARTED -eq 0 ]; then
        	echo -e '\E[1;31mcoreography has not been started ...'; tput sgr0
	else
                ./scripts/stopChor.sh
                CHOR_STARTED=0;
		echo -e "\033[1mcoreography stopped...\033[0m"
        fi
	  
  elif [ $line == "exit" ]; then
	./scripts/stopChor.sh
	exit 0

  elif [ $line == "run_unit-tests" ]; then
   	ant runUnitTests | grep -v junitreport
	echo ""
	echo -e "\033[1mtest reports saved in ./test-reports/unit dir....\033[0m"

  elif [ $line == "run_integration-tests" ]; then
	ant runIntegrationTests | grep -v junitreport
	echo ""
	echo -e "\033[1mtest reports saved in ./test-reports/integration dir....\033[0m"

  elif [ $line == "run_acceptance-tests" ]; then
	ant runAcceptanceTests | grep -v junitreport
	echo ""
	echo -e "\033[1mtest reports saved in ./test-reports/acceptance dir....\033[0m"

  elif [ $line == "run_all-tests" ]; then
 	ant runAllTests | grep -v junitreport
	echo ""
 	echo -e "\033[1mtest reports saved in test-reports/all dir....\033[0m"
  
	elif [ $line == "help" ]; then
		help_message

  else
	echo "invalid command"
  
  fi
	
  done

exit 0


