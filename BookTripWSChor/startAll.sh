#!/bin/bash

# Determine local host's IP
clear
echo -n "Determining local host's IP... "
IP=`java -cp lib/ScanIP.jar Main`
echo "$IP"

# Log files
LOG_DS="log/DS.log";
LOG_PEER_PUBLISHER="log/PeerPublisher.log";
LOG_PEER_TRAVELER="log/Traveler.log";
LOG_PEER_TRAVELAGENCY="log/TravelAgency.log";
LOG_PEER_AIRLINE="log/AirLine.log";
LOG_PEER_ACQUIRER="log/Acquirer.log";

# Classpath
CP_KERNEL=$(cat "config/classpath_ok")
CP_DEMO="build/BookTrip.jar";
CP="$CP_KERNEL:$CP_DEMO";

ARGS_WSs="-jar lib/chore-ws.jar ChoreWS all";
ARGS_QUEUE="-cp $CP br.usp.ime.booktrip.utils.MessageTraceQueue"
ARGS_DS="-cp $CP org.openk.service.discovery.StartDiscoveryAndStorage $IP 6678 6678 7000 true 10";
ARGS_PEER_PUBLISHER="-cp $CP br.usp.ime.booktrip.peer.PeerPublisher -discovery-bootstrap-host $IP -port-number 5007 -userID PeerPublisher@openk.org";
ARGS_PEER_TRAVELER="-cp $CP br.usp.ime.booktrip.peer.Traveler -discovery-bootstrap-host $IP -port-number 5008 -userID Traveler@openk.org";
ARGS_PEER_TRAVELAGENCY="-cp $CP br.usp.ime.booktrip.peer.TravelAgency -discovery-bootstrap-host $IP -port-number 5009 -userID TravelAgency@openk.org";
ARGS_PEER_AIRLINE="-cp $CP br.usp.ime.booktrip.peer.AirLine -discovery-bootstrap-host $IP -port-number 5010 -userID AirLine@openk.org";
ARGS_PEER_ACQUIRER="-cp $CP br.usp.ime.booktrip.peer.Acquirer -discovery-bootstrap-host $IP -port-number 5011 -userID Acquirer@openk.org";

#Clean databases
rm -r *.db
#Commands
echo -n "Stopping currently running instances... "
./stopAll.sh
for a in `ps aux | grep "java -jar lib/chore-ws.jar" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done
echo "Done"

echo -n "Lauching the web services... "
java $ARGS_WSs &
sleep 5
echo "Done"

#Start queue
echo -n "Starting message trace queue... "
java $ARGS_QUEUE
echo "Done"

echo -n "Launching the Discovery Service... "
rm -rf FreePastry-Storage-Root/
java $ARGS_DS > $LOG_DS &
sleep 10
echo "Done"

echo -n "Publishing the Interaction Model... "
java $ARGS_PEER_PUBLISHER > $LOG_PEER_PUBLISHER &
sleep 5
echo "Done"

echo -n "Launching the traveler... "
java $ARGS_PEER_TRAVELER > $LOG_PEER_TRAVELER &
sleep 4
echo "Done"

echo -n "Launching the travel agency... "
java $ARGS_PEER_TRAVELAGENCY > $LOG_PEER_TRAVELAGENCY &
sleep 4
echo "Done"

echo -n "Launching the airLine... "
java $ARGS_PEER_AIRLINE > $LOG_PEER_AIRLINE &
sleep 4
echo "Done"

echo -n "Launching the acquirer... "
java $ARGS_PEER_ACQUIRER > $LOG_PEER_ACQUIRER &
sleep 4
echo "Done"


read -n1 -p "Press any key to quit..."
./stopAll.sh
for a in `ps aux | grep "java -jar lib/chore-ws.jar" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done

echo ""

