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

        int randomIndexClients;
        int randomIndexCurrencies;


        ArrayList<String> clients_list = new ArrayList<String>();
        ArrayList<String> currency_list = new ArrayList<String>();

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

        //consumer topic currency
        Properties props3 = new Properties();
        props3.put("bootstrap.servers", "localhost:9092");
        props3.put("acks", "all");
        props3.put("retries", 0);
        props3.put("batch.size", 16384);
        props3.put("linger.ms", 1);
        props3.put("buffer.memory", 33554432);
        props3.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaCurrencyConsumer");
        props3.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props3.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        Consumer<String, String> consumerClients = new KafkaConsumer<>(props2);
        consumerClients.subscribe(Collections.singletonList("client_list"));

        Consumer<String, String> consumerCurrency = new KafkaConsumer<>(props3);
        consumerCurrency.subscribe(Collections.singletonList("currency_list"));

        consumerClients.poll(0);
        consumerClients.seekToBeginning(consumerClients.assignment());

        consumerCurrency.poll(0);
        consumerCurrency.seekToBeginning(consumerCurrency.assignment());

        while(true){

            String s;

            ConsumerRecords<String, String> records = consumerClients.poll(0);
            for (ConsumerRecord<String, String> record : records) {
                //System.out.printf(record.value());
                s = deserializer(record.value());
                clients_list.add(s);
            }

            System.out.println("LISTA DE CLIENTES: " + clients_list.toString());

            ConsumerRecords<String, String> recordsCurrency = consumerCurrency.poll(100);
            for (ConsumerRecord<String, String> record : recordsCurrency) {
                //System.out.printf(record.value());
                String moeda = (String) new JSONObject(record.value()).getJSONObject("payload").get("name");
                currency_list.add(moeda);
            }

            System.out.println("LISTA DE CURRENCIES: " + currency_list.toString());

            if (clients_list.size() != 0 && currency_list.size() != 0) {

                randomIndexClients = rand.nextInt(clients_list.size());
                randomIndexCurrencies = rand.nextInt(currency_list.size());

                int payment = rand.nextInt(1000);
                Double newPayment = Double.parseDouble(String.valueOf(payment));

                String jsonStringPayment = new JSONObject()
                        .put("amount", newPayment.toString())
                        .put("currency", currency_list.get(randomIndexCurrencies))
                        .toString();
                producer.send(new ProducerRecord<String, String>(paymentsTopic, clients_list.get(randomIndexClients), jsonStringPayment));

                System.out.println("Sending message " + clients_list.get(randomIndexClients) + " made a payment:" + newPayment + " "+ currency_list.get(randomIndexCurrencies) + " to topic " + paymentsTopic);

                /*
                producer.send(new ProducerRecord<String, String>(paymentsTopic, clients_list.get(randomIndex), "{\"amount\":\"" + newPayment.toString() + "\",\"currency\":\"" + currency + "\"}"));
                */


                randomIndexClients = rand.nextInt(clients_list.size());
                randomIndexCurrencies = rand.nextInt(currency_list.size());

                int credit = rand.nextInt(1000);
                Double newCredit = Double.parseDouble(String.valueOf(credit));

                String jsonStringCredit = new JSONObject()
                        .put("amount", newCredit.toString())
                        .put("currency", currency_list.get(randomIndexCurrencies))
                        .toString();
                producer.send(new ProducerRecord<String, String>(creditsTopic, clients_list.get(randomIndexClients), jsonStringCredit));

                System.out.println("Sending message " + clients_list.get(randomIndexClients) + " made a credit:" + newCredit + " "+ currency_list.get(randomIndexCurrencies) + " to topic " + creditsTopic);

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