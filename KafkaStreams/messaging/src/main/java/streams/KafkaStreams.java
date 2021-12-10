package streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

public class KafkaStreams {
    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length != 2) {
            System.err.println("Wrong arguments. Please run the class as follows:");
            System.err.println(SimpleStreamsExercisesb.class.getName() + " input-topic output-topic");
            System.exit(1);
        }
        String topicName = args[0].toString();
        String outtopicname = args[1].toString();
        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> payments = builder.stream("paymentstopic");

        KTable<String, String> pagamentos = payments.
                groupByKey().
                reduce((v1, v2) -> "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}");

        pagamentos.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"" + k + "\"}}"
        ).toStream().to("client_payments");


        KStream<String, String> credits = builder.stream("creditstopic");

        KTable<String, String> creditos = credits.
                groupByKey().
                reduce((v1, v2) -> "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}");

        creditos.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"" + k + "\"}}"
        ).toStream().to("client_credits");

        org.apache.kafka.streams.KafkaStreams streams = new org.apache.kafka.streams.KafkaStreams(builder.build(), props);
        streams.start();
    }

    public static Double get_amount(String record){
        JSONObject obj = new JSONObject(record);
        String s = obj.getString("amount");
        Double amount = Double.parseDouble(s);
        return amount;
    }

}