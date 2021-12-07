# IS-Projeto3
------------ COMANDOS ---------------
		
		cd workspace/
		
		mvn clean package wildfly:deploy

#Topicos do Cliente

		java -cp messaging/target/messaging.jar kafka.Client creditsTopic paymentsTopic

#Kafka Streams

		java -cp messaging/target/messaging.jar streams.KafkaStreams paymentsTopic resultsTopic

--------- COMANDOS DEBUG ----------

Enviar um comando para a base de dados

		/opt/kafka_2.12-2.8.1/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic resultsTopic
		
Ver o que tem a base de dados

		/opt/kafka_2.12-2.8.1/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dbinfoTopic --from-beginning
		
Dar debug ao paymentsTopic

		java -jar target/KafkaStreams.jar paymentsTopic
		
		java -cp messaging/target/messaging.jar kafka.Client creditsTopic paymentsTopic

Dar debug ao resultsTopic

		java -cp messaging/target/messaging.jar kafka.Client creditsTopic paymentsTopic
		
		java -cp messaging/target/messaging.jar streams.KafkaStreams paymentsTopic resultsTopic

		/opt/kafka_2.12-2.8.1/bin/kafka-console-consumer.sh --topic resultsTopic --from-beginning --bootstrap-server localhost:9092
		
