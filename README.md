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

	/opt/kafka_2.12-2.8.1/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic client_list --from-beginning
		
Dar debug ao paymentsTopic

	java -jar messaging/target/messaging.jar paymentstopic
		
	java -cp messaging/target/messaging.jar kafka.Client creditsTopic paymentstopic

Dar debug ao resultsTopic

	/opt/kafka_2.12-2.8.1/bin/kafka-console-consumer.sh --topic mytopic --from-beginning --bootstrap-server localhost:9092
		
	java -cp messaging/target/messaging.jar streams.KafkaStreams paymentstopic mytopic
		
	java -cp messaging/target/messaging.jar kafka.Client creditstopic paymentstopic
		
		Se não der troca-se a ordem dos dois últimos comandos
	
Eliminar um tópico

	/opt/kafka_2.12-2.8.1/bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic resultstopic
		
Reiniciar sink

	curl -X POST http://localhost:8083/connectors/jdbc-sink-filipe/tasks/0/restart

Ver status

	curl localhost:8083/connectors/jdbc-sink-filipe/status

Inserir dados

	INSERT INTO currency (id, name, conversion) VALUES (1, 'USD', 0.89);
	INSERT INTO currency (id, name, conversion) VALUES (2, 'GBP', 1.17);
	INSERT INTO currency (id, name, conversion) VALUES (2, 'CHF', 0.96);
	INSERT INTO currency (id, name, conversion) VALUES (3, 'NZD', 0.60);
	INSERT INTO currency (id, name, conversion) VALUES (4, 'JPY', 0.0078);
	INSERT INTO currency (id, name, conversion) VALUES (5, 'CAD', 0.70);
	INSERT INTO currency (id, name, conversion) VALUES (6, 'AUD', 0.63);
	INSERT INTO currency (id, name, conversion) VALUES (7, 'CZK', 0.039);
	INSERT INTO currency (id, name, conversion) VALUES (8, 'MXN', 0.042);
	INSERT INTO currency (id, name, conversion) VALUES (9, 'MXN', 0.098);

	INSERT INTO client (nome, email) VALUES ('Nelso','nelso@email.com');
	
