# IS-Projeto3
------------ COMANDOS ---------------
		
		cd workspace/
		
		mvn clean package wildfly:deploy

#Topicos do Cliente

		java -jar messaging/target/messaging.jar mytopic

		java -cp messaging/target/messaging.jar kafka.SimpleProducer mytopic

#Kafka Streams

		java -cp messaging/target/messaging.jar streams.SimpleStreamsExercisesb kstreamstopic resultstopic

#Topicos Result

		/opt/kafka_2.12-2.8.1/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic project3toDB
