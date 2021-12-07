#!/bin/bash
# Start the first process
/opt/kafka_2.12-2.8.1/bin/connect-standalone.sh /opt/kafka_2.12-2.8.1/config/connect-standalone.properties /opt/kafka_2.12-2.8.1/config/connect-jdbc-source-filipe.properties /opt/kafka_2.12-2.8.1/config/connect-jdbc-sink-filipe.properties
