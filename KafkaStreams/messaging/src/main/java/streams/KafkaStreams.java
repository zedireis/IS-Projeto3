package streams;

import kafka.ListConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaStreams {

    private static ListConsumer consumer;

    public static void main(String[] args) throws InterruptedException, IOException {

        java.util.Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> payments = builder.stream("paymentstopic");

        //Soma de pagamentos para cada cliente
        KTable<String, String> pagamentos = payments.
                groupByKey().
                reduce((v1, v2) -> {
                    //System.out.println("AQUI->"+v1+v2);
                    //System.out.println("AQUI->"+consumer.getClients_list().toString());
                    return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
                }
                );

        pagamentos.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"" + k + "\"}}"
        ).toStream().to("client_payments");


        //Soma de creditos para cada cliente
        KStream<String, String> credits = builder.stream("creditstopic");

        KTable<String, String> creditos = credits.
                groupByKey().
                reduce((v1, v2) -> "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}");

        creditos.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"" + k + "\"}}"
        ).toStream().to("client_credits");


        //Saldo de cada cliente
        KTable<String, String> balanceByUser = pagamentos
                .outerJoin(creditos, (payment, credit) -> {
                    //System.out.println("Pagamentos"+payment+"<> Credit"+credit);
                    if(credit == null){
                        return "{\"amount\":\"" + (0 - get_amount(payment)) + "\",\"currency\":\"EUR\"}";
                    }else if(payment == null){
                        return "{\"amount\":\"" + get_amount(credit) + "\",\"currency\":\"EUR\"}";
                    }
                    return "{\"amount\":\"" + (get_amount(credit) - get_amount(payment)) + "\",\"currency\":\"EUR\"}";
                });

        balanceByUser.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"" + k + "\"}}"
        ).toStream().to("balance");


        //Soma de todos os pagamentos
        KTable<String, String> totalPayments = pagamentos.toStream().selectKey((k,v) -> "pagamentos").groupByKey().reduce((v1, v2) -> {
            return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
        });
        totalPayments.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"pagamentos\"}}"
        ).toStream().to("total");


        //Soma de todos os creditos
        KTable<String, String> totalCredits = pagamentos.toStream().selectKey((k,v) -> "creditos").groupByKey().reduce((v1, v2) -> {
            return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
        });
        totalCredits.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"creditos\"}}"
        ).toStream().to("total");

        //Last month bill
        KTable<Windowed<String>, String> lastMonthPayments = payments.
                groupByKey().
                windowedBy(TimeWindows.of(Duration.ofDays(30))).
                reduce((v1, v2) -> {
                            //System.out.println("AQUI->"+v1+v2);
                            return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
                });
        KTable<Windowed<String>, String> lastMonthCredits = credits.
                groupByKey().
                windowedBy(TimeWindows.of(Duration.ofDays(30))).
                reduce((v1, v2) -> {
                    //System.out.println("AQUI->"+v1+v2);
                    return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
                });
        KTable<Windowed<String>, String> lastMonthBalance = lastMonthCredits
                .outerJoin(lastMonthPayments, (payment, credit) -> {
                    //System.out.println("Pagamentos"+payment+"<> Credit"+credit);
                    if(credit == null){
                        return "{\"amount\":\"" + (0 - get_amount(payment)) + "\",\"currency\":\"EUR\"}";
                    }else if(payment == null){
                        return "{\"amount\":\"" + get_amount(credit) + "\",\"currency\":\"EUR\"}";
                    }
                    return "{\"amount\":\"" + (get_amount(credit) - get_amount(payment)) + "\",\"currency\":\"EUR\"}";
                });

        lastMonthBalance
        .toStream((wk, v) -> wk.key()).map((k,v) -> new KeyValue<>(k,
                "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\""+k+"\"}}"
        )).to("last_month_bill",Produced.with(Serdes.String(), Serdes.String()));

        //2 Month without payments
        KTable<String, String> users = builder.stream("client_list", Consumed.with(Serdes.String(), Serdes.String())).toTable();

        KTable<String, String> twoMonth = payments.groupByKey().windowedBy(TimeWindows.of(Duration.ofDays(60))).reduce((k,v)->k).toStream((wk, v) -> wk.key()).toTable();

        users.toStream().foreach((k,v) -> System.out.println("Users->"+k+v));
        twoMonth.toStream().foreach((k,v) -> System.out.println("TwoMonth->"+k+v));

        KTable<String, String> noPayments = users.outerJoin(twoMonth, (v1,v2) -> {
            System.out.println("Teste->"+v1+v2);
            return "AQUI";
        });

        //Manager revenue
        //pagamentos.toStream();


        org.apache.kafka.streams.KafkaStreams streams = new org.apache.kafka.streams.KafkaStreams(builder.build(), props);
        try {
            streams.cleanUp();
            streams.start();
            consumer = new ListConsumer();
            consumer.start();
        }catch (Exception e){
            System.exit(1);
        }

    }

    public static Double get_amount(String record){
        JSONObject obj = new JSONObject(record);
        String s = obj.getString("amount");
        String moeda = obj.getString("currency");
        Double conversion = consumer.getConversion(moeda);
        Double amount = Double.parseDouble(s);
        amount = amount * conversion;
        //System.out.println(record+" CONVERSAO->"+amount);
        return amount;
    }

    public static String get_email(String record){
        JSONObject obj = new JSONObject(record).getJSONObject("payload");;
        String email = obj.getString("email");
        return email;
    }

    public static String get_manager(String record){
        JSONObject obj = new JSONObject(record).getJSONObject("payload");;
        String email = obj.getString("manager_email");
        return email;
    }

}