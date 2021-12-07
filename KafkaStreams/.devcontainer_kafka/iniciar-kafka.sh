#!/bin/bash
# Start the first process
opt/kafka_2.12-2.8.1/bin/zookeeper-server-start.sh /opt/kafka_2.12-2.8.1/config/zookeeper.properties &

sleep 20

# Start the second process
opt/kafka_2.12-2.8.1/bin/kafka-server-start.sh /opt/kafka_2.12-2.8.1/config/server.properties &

sleep 20

#Iniciar sink e source
opt/kafka_2.12-2.8.1/bin/iniciar-dbconnection.sh &

#ZSH
zsh &

# Wait for any process to exit
wait -n
# Exit with status of process that exited first
exit $?