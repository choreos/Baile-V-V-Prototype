#!/bin/bash

# Determine local host's IP
clear
echo -n "Determining local host's IP... "
IP=`java -cp lib/ScanIP.jar Main`
echo "$IP"

# Log files
LOG_PEER_ACQUIRER="log/Acquirer.log";


# Classpath
CP_KERNEL="config/:lib/ok.jar:lib/FreePastry-2.0b.jar:lib/asm-2.2.1.jar:lib/bcel-5.2.jar:lib/cglib-2.2_beta1.jar:lib/colt.jar:lib/discovery.jar:lib/hsqldb.jar:lib/jmock-1.1.0.jar:lib/jmock-cglib-1.1.0.jar:lib/joda-time-1.4.jar:lib/lcc-interpreter-wrapper.jar:lib/lcc-interpreter.jar:lib/log4j-1.2.13.jar:lib/log4j-1.2.14.jar:lib/matcher.jar:lib/prefuse.jar:lib/silib.jar:lib/tree.jar:lib/xmlpull_1_1_3_4a.jar:lib/xpp3-1.1.3.4d_b2.jar:lib/jdom/jdom.jar:lib/smatch/OKsmatch.jar:lib/smatch/orbital-core.jar:lib/smatch/jgrapht-0.6.0.jar:lib/smatch/orbital-ext.jar:lib/smatch/jsat.jar:lib/smatch/sat4j-1.5new.jar:lib/smatch/jwnl.jar:lib/smatch/xercesImpl.jar:lib/smatch/minilearningbr.jar:lib/smatch/xmlParserAPIs.jar";
CP_DEMO="build/BookTrip.jar";
CP="$CP_KERNEL:$CP_DEMO";

#Airline
ARGS_PEER_AIRLINE="-cp $CP br.usp.ime.booktrip.peer.Acquirer -discovery-bootstrap-host $IP -port-number 5010 -userID Acquirer@openk.org";

echo -n "Launching the airLine... "
java $ARGS_PEER_ACQUIRER > $LOG_PEER_ACQUIRER &
sleep 3
echo "Done"

echo -e "\nRunning..."
#read -n1 -p "Press any key to quit..."

#for a in `ps aux | grep "br.usp.ime.booktrip.AirLine" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done

#echo ""
