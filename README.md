# IS-Projeto3
------------ COMANDOS ---------------
		
		cd workspace/
		
		mvn clean package wildfly:deploy

#Topicos do Cliente

		java -cp messaging/target/messaging.jar kafka.Client creditstopic paymentstopic

#Kafka Streams

		java -cp messaging/target/messaging.jar streams.KafkaStreams paymentstopic resultstopic

--------- COMANDOS DEBUG ----------

Enviar um comando para a base de dados

		/opt/kafka_2.12-2.8.1/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic resultstopic
		
Ver o que tem a base de dados

		/opt/kafka_2.12-2.8.1/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dbinfotopic --from-beginning
		
Dar debug ao paymentsTopic

		java -jar messaging/target/messaging.jar paymentstopic
		
		java -cp messaging/target/messaging.jar kafka.Client creditsTopic paymentstopic

Dar debug ao resultsTopic

		/opt/kafka_2.12-2.8.1/bin/kafka-console-consumer.sh --topic mytopic --from-beginning --bootstrap-server localhost:9092
		
		java -cp messaging/target/messaging.jar streams.KafkaStreams paymentsTopic mytopic
		
		java -cp messaging/target/messaging.jar kafka.Client creditsTopic paymentstopic
		
	Se não der troca-se a ordem dos dois últimos comandos
	
Eliminar um tópico

		/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic resultstopic
		
Reiniciar sink

		curl -X POST http://localhost:8083/connectors/jdbc-sink-filipe/tasks/0/restart


