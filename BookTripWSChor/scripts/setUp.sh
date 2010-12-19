#!/bin/bash

# Determine local host's IP
# -------------------------------------------------
echo ""
echo -n "Determining localhost's IP...  "
IP=`java -cp lib/ScanIP.jar Main`
echo -e "\E[1;34m$IP"; tput sgr0


# Log files
# -------------------------------------------------
LOG_ANT="log/ANT.log";
LOG_DS="log/DS.log";
LOG_WS="log/WS.log";
LOG_QUEUE="log/Queue.log";
LOG_PEER_PUBLISHER="log/PeerPublisher.log";
LOG_PEER_TRAVELER="log/Traveler.log";
LOG_PEER_TRAVELAGENCY="log/TravelAgency.log";
LOG_PEER_AIRLINE="log/AirLine.log";
LOG_PEER_ACQUIRER="log/Acquirer.log";


# Classpath
# -------------------------------------------------
CP_KERNEL=$(cat "config/classpath_ok")
CP_DEMO="build/BookTrip.jar";
CP="$CP_KERNEL:$CP_DEMO";


# Args
# -------------------------------------------------
ARGS_WS="-jar lib/chore-ws.jar ChoreWS all";
ARGS_QUEUE="-cp $CP br.usp.ime.booktrip.utils.MessageTraceQueue"
ARGS_DS="-cp $CP org.openk.service.discovery.StartDiscoveryAndStorage $IP 6678 6678 7000 true 10";
ARGS_PEER_PUBLISHER="-cp $CP br.usp.ime.booktrip.peer.PeerPublisher -discovery-bootstrap-host $IP -port-number 5007 -userID PeerPublisher@openk.org";
ARGS_PEER_TRAVELER="-cp $CP br.usp.ime.booktrip.peer.Traveler -discovery-bootstrap-host $IP -port-number 5008 -userID Traveler@openk.org";
ARGS_PEER_TRAVELAGENCY="-cp $CP br.usp.ime.booktrip.peer.TravelAgency -discovery-bootstrap-host $IP -port-number 5009 -userID TravelAgency@openk.org";
ARGS_PEER_AIRLINE="-cp $CP br.usp.ime.booktrip.peer.AirLine -discovery-bootstrap-host $IP -port-number 5010 -userID AirLine@openk.org";
ARGS_PEER_ACQUIRER="-cp $CP br.usp.ime.booktrip.peer.Acquirer -discovery-bootstrap-host $IP -port-number 5011 -userID Acquirer@openk.org";


# Stopping running instances
# -------------------------------------------------
echo -n "Stopping currently running instances... "
./scripts/stopChor.sh
rm -rf ../db/*.db
for a in `ps aux | grep "java -jar lib/chore-ws.jar" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done
echo -e '\E[1;32mDone'; tput sgr0


# Compiling choreography
# -------------------------------------------------
echo -n "Compiling components... "
ant > $LOG_ANT
echo -e '\E[1;32mDone'; tput sgr0


# Publishing web services
# -------------------------------------------------
echo -n "Publishing the web services... "
java $ARGS_WS > $LOG_WS 2>&1 &
sleep 5
echo -e '\E[1;32mDone'; tput sgr0


# Starting queue
# -------------------------------------------------
echo -n "Starting message trace queue... "
java $ARGS_QUEUE > $LOG_QUEUE 2>&1 &
echo -e '\E[1;32mDone'; tput sgr0


# Launching DS
# -------------------------------------------------
echo -n "Launching the Discovery Service... "
rm -rf FreePastry-Storage-Root/
java $ARGS_DS > $LOG_DS &
sleep 12
echo -e '\E[1;32mDone'; tput sgr0


# Publishing IM
# -------------------------------------------------
echo -n "Publishing the Interaction Model... "
java $ARGS_PEER_PUBLISHER > $LOG_PEER_PUBLISHER &
sleep 10
echo -e '\E[1;32mDone'; tput sgr0


# Launching traveler
# -------------------------------------------------
echo -n "Launching the traveler... "
java $ARGS_PEER_TRAVELER > $LOG_PEER_TRAVELER &
sleep 4
echo -e '\E[1;32mDone'; tput sgr0


# Launching travel agency
# -------------------------------------------------
echo -n "Launching the travel agency... "
java $ARGS_PEER_TRAVELAGENCY > $LOG_PEER_TRAVELAGENCY &
sleep 4
echo -e '\E[1;32mDone'; tput sgr0


# Lauching airline
# -------------------------------------------------
echo -n "Launching the airLine... "
java $ARGS_PEER_AIRLINE > $LOG_PEER_AIRLINE &
sleep 4
echo -e '\E[1;32mDone'; tput sgr0

# Launching acquirer
# -------------------------------------------------
echo -n "Launching the acquirer... "
java $ARGS_PEER_ACQUIRER > $LOG_PEER_ACQUIRER &
sleep 4
echo -e '\E[1;32mDone'; tput sgr0

# Finishing setting up
# -------------------------------------------------
echo -n "Setting up roles... "
sleep 20
echo -e '\E[1;32mDone'; tput sgr0
echo -e '\E[1;33mChoreography started.'; tput sgr0

