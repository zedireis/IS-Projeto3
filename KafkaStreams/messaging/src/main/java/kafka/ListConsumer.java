package kafka;

import org.apache.kafka.clients.consumer.*;
import org.json.JSONObject;

import java.util.*;

public class ListConsumer extends Thread{

    private Map<String, String> clients_list;
    private Map<String, Double> currency_list;

    private Properties props2;
    private Properties props3;

    private Consumer<String, String> consumerClients;
    private Consumer<String, String> consumerCurrency;

    public ListConsumer() {
        clients_list = new HashMap<String, String>();
        currency_list = new HashMap<String, Double>();

        props2 = new Properties();
        props2.put("bootstrap.servers", "localhost:9092");
        props2.put("acks", "all");
        props2.put("retries", 0);
        props2.put("batch.size", 16384);
        props2.put("linger.ms", 1);
        props2.put("buffer.memory", 33554432);
        props2.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaClientConsumer");
        props2.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props2.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props2.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //consumer topic currency
        props3 = new Properties();
        props3.put("bootstrap.servers", "localhost:9092");
        props3.put("acks", "all");
        props3.put("retries", 0);
        props3.put("batch.size", 16384);
        props3.put("linger.ms", 1);
        props3.put("buffer.memory", 33554432);
        props3.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaCurrencyConsumer2");
        props3.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props3.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props3.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @Override
    public void run() {
        consumerClients = new KafkaConsumer<>(props2);
        consumerClients.subscribe(Collections.singletonList("client_list"));

        consumerCurrency = new KafkaConsumer<>(props3);
        consumerCurrency.subscribe(Collections.singletonList("currency_list"));

        consumerClients.poll(0);
        consumerClients.seekToBeginning(consumerClients.assignment());

        consumerCurrency.poll(0);
        consumerCurrency.seekToBeginning(consumerCurrency.assignment());

        String email, manager_email, moeda;
        Double conversion;
        Integer sleep_time = 500;

        JSONObject currency, client;

        System.out.println("A ir buscar clientes e currencies");

        while(true) {

            ConsumerRecords<String, String> records = consumerClients.poll(0);
            for (ConsumerRecord<String, String> record : records) {
                //System.out.printf(record.value());
                client = new JSONObject(record.value()).getJSONObject("payload");
                email = client.getString("email");
                manager_email = client.getString("manager_email");
                clients_list.put(email, manager_email);
            }
            //System.out.println("LISTA DE CLIENTES: " + clients_list.toString());

            ConsumerRecords<String, String> recordsCurrency = consumerCurrency.poll(100);
            for (ConsumerRecord<String, String> record : recordsCurrency) {
                //System.out.printf(record.value());

                currency = new JSONObject(record.value()).getJSONObject("payload");
                moeda = (String) currency.get("name");
                conversion = currency.getBigDecimal("conversion").doubleValue();
                currency_list.put(moeda,conversion);

            }

            //System.out.println("LISTA DE CURRENCIES: " + currency_list.toString());

            if (clients_list.size() != 0 && currency_list.size() != 0) {
                sleep_time = 30000;
            }

            try {
                Thread.sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        consumerClients.close();
        consumerCurrency.close();
    }

    public String getManager(String cliente){
        //System.out.println(clients_list.toString());
        if(clients_list.containsKey(cliente)) {
            return clients_list.get(cliente).toString();
        }else{
            return null;
        }
    }

    public Double getConversion(String money){
        //System.out.println("AQUI: " + currency_list.toString());
        //System.out.println(currency_list.toString());
        if(currency_list.containsKey(money)) {
            return currency_list.get(money).doubleValue();
        }else{
            return 1.0;
        }
    }
}
