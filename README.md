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
	
Eliminar tópicos

	/workspace/.devcontainer_kafka/reset-topics.sh
		
Reiniciar sink

	curl -X POST http://localhost:8083/connectors/jdbc-sink-filipe/tasks/0/restart

Ver status

	curl localhost:8083/connectors/jdbc-sink-filipe/status

Inserir dados

	INSERT INTO currency (name, conversion) VALUES ('USD', 0.89);
	INSERT INTO currency (name, conversion) VALUES ('GBP', 1.17);
	INSERT INTO currency (name, conversion) VALUES ('CHF', 0.96);
	INSERT INTO currency (name, conversion) VALUES ('NZD', 0.60);
	INSERT INTO currency (name, conversion) VALUES ('JPY', 0.0078);
	INSERT INTO currency (name, conversion) VALUES ('CAD', 0.70);
	INSERT INTO currency (name, conversion) VALUES ('AUD', 0.63);
	INSERT INTO currency (name, conversion) VALUES ('CZK', 0.039);
	INSERT INTO currency (name, conversion) VALUES ('MXN', 0.042);

	INSERT INTO manager (nome, email) VALUES ('Nelso','nelso@email.com');

	INSERT INTO client (nome, email,manager_email) VALUES ('Gervasio','gervasio@email.com','nelso@email.com');
	INSERT INTO client (nome, email,manager_email) VALUES ('Tiburcio','tiburcio@email.com','nelso@email.com');
	
