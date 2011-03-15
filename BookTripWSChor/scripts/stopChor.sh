#!/bin/sh

for a in `ps aux | grep "java -cp config/" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done
for a in `ps aux | grep "java -jar lib/chore-ws.jar" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done

for a in `ps aux | grep "java -cp lib/chore-ws_lib/hsqldb.jar org.hsqldb.Server -database.0 file:db/okQueue.hsqldb -dbname.0 okQueue.hsqldb" | grep -v grep | awk '{print $2}'`; do kill -9 $a; done

