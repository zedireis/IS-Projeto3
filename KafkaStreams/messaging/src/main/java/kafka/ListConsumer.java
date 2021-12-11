package kafka;

import org.apache.kafka.clients.consumer.*;
import org.json.JSONObject;

import java.util.*;

public class ListConsumer extends Thread{

    private ArrayList<String> clients_list;
    private Map<String, Double> currency_list;

    private Properties props2;
    private Properties props3;

    public ListConsumer() {
        clients_list = new ArrayList<String>();
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
    }

    @Override
    public void run() {
        Consumer<String, String> consumerClients = new KafkaConsumer<>(props2);
        consumerClients.subscribe(Collections.singletonList("client_list"));

        Consumer<String, String> consumerCurrency = new KafkaConsumer<>(props3);
        consumerCurrency.subscribe(Collections.singletonList("currency_list"));

        consumerClients.poll(0);
        consumerClients.seekToBeginning(consumerClients.assignment());

        consumerCurrency.poll(0);
        consumerCurrency.seekToBeginning(consumerCurrency.assignment());

        String client, moeda;
        Double conversion;
        Integer sleep_time = 5000;

        JSONObject currency;

        System.out.println("A ir buscar clientes e currencies");

        while(true) {

            ConsumerRecords<String, String> records = consumerClients.poll(0);
            for (ConsumerRecord<String, String> record : records) {
                //System.out.printf(record.value());
                client = deserializer(record.value());
                clients_list.add(client);
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

    public ArrayList<String> getClients_list() {
        return clients_list;
    }

    public Double getConversion(String money){
        //System.out.println("AQUI: " + currency_list.toString());
        if(currency_list.containsKey(money)) {
            return currency_list.get(money).doubleValue();
        }else{
            return 1.0;
        }
    }

    public static String deserializer(String record){
        return (String) new JSONObject(record).getJSONObject("payload").get("email");
    }
}
