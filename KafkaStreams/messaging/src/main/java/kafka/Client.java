package kafka;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.RecursiveTask;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.*;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;

public class Client {



    public static void main(String[] args) throws Exception{
        Random rand = new Random();

        ArrayList<String> clients_list = new ArrayList<String>();

//Assign topicName to string variable
        String creditsTopic = "creditstopic";
        String paymentsTopic = "paymentstopic";


// create instance for properties to access producer configs
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        Properties props2 = new Properties();
        props2.put("bootstrap.servers", "localhost:9092");
        props2.put("acks", "all");
        props2.put("retries", 0);
        props2.put("batch.size", 16384);
        props2.put("linger.ms", 1);
        props2.put("buffer.memory", 33554432);
        props2.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props2.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props2.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        Consumer<String, String> consumerClients = new KafkaConsumer<>(props2);
        consumerClients.subscribe(Collections.singletonList("client_list"));

        /*
        Consumer<String, String> consumerCurrency = new KafkaConsumer<>(props);
        consumerCurrency.subscribe(Collections.singletonList("currency_list"));*/

        consumerClients.poll(0);
        consumerClients.seekToBeginning(consumerClients.assignment());

        while(true){

            String s;

            ConsumerRecords<String, String> records = consumerClients.poll(0);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf(record.value());
                s = deserializer(record.value());
                clients_list.add(s);
            }

            System.out.println("LISTA DE CLIENTES: " + clients_list.toString());

            if (clients_list.size() != 0) {

                int randomIndex = rand.nextInt(clients_list.size());
                int credit = rand.nextInt(1000);
                int payment = rand.nextInt(1000);
                Double newPayment = Double.parseDouble(String.valueOf(payment));
                String currency = "EUR";


                producer.send(new ProducerRecord<String, String>(paymentsTopic, clients_list.get(randomIndex), "{\"amount\":\"" + newPayment.toString() + "\",\"currency\":\"" + currency + "\"}"));

                System.out.println("Sending message " + clients_list.get(randomIndex) + " made a payment:" + newPayment + " to topic " + paymentsTopic);

                Thread.sleep(30000);
            }else{
                Thread.sleep(5000);
            }
        }

    }

    public static String deserializer(String record){
        return (String) new JSONObject(record).getJSONObject("payload").get("email");
    }

}