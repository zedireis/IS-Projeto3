#!/bin/bash

/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic paymentstopic

/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic creditstopic

/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic client_payments

/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic client_credits

/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic balance

/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic mytopic

curl -X POST http://localhost:8083/connectors/jdbc-sink-filipe/tasks/0/restart
