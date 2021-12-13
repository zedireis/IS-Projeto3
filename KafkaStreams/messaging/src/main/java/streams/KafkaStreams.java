package streams;

import kafka.ListConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.KTableAggregate;
import org.json.JSONObject;

import javax.print.attribute.standard.JobKOctets;
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
        KTable<String, String> totalPayments = payments.selectKey((k,v) -> "pagamentos").groupByKey().reduce((v1, v2) -> {
            //System.out.println("Total payments->"+v1+v2);
            return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
        });
        totalPayments.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"pagamentos\"}}"
        ).toStream().to("total");

        //Soma de todos os creditos
        KTable<String, String> totalCredits = credits.selectKey((k,v) -> "creditos").groupByKey().reduce((v1, v2) -> {
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
        KTable<Windowed<String>, String> lastMonthBalance = lastMonthPayments
                .outerJoin(lastMonthCredits, (payment, credit) -> {
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
        KStream<String, Object> users = builder.stream("client_list").selectKey((k,v)->get_email((String) v));

        //users.foreach((k,v)->System.out.println("Users->"+k+v));

        KTable<String, Object> userTable = users.toTable();

        KTable<Windowed<String>, Long> aux = payments.groupByKey().windowedBy(TimeWindows.of(Duration.ofDays(60))).count();
        KTable<String, String> twoMonth = aux.toStream((wk, v) -> wk.key()).map((k,v) -> new KeyValue<>(k,String.valueOf(v))).toTable();

        //twoMonth.toStream().foreach((k,v) -> System.out.println("TwoMonth->"+k+v));

        KTable<String, String> noPayments = userTable.outerJoin(twoMonth, (v1,v2) -> {
            if(v1 == null){
                return v2;
            }
            if(v2 == null){
                return "0";
            }
            return v2;
        });
        noPayments.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + Double.parseDouble(v) + ",\"client_email\":\""+k+"\"}}"
        ).toStream().to("two_month_payments");

        //Manager revenue
        KTable<String, String> managerCredits = payments.selectKey((k,v) -> get_manager((String) k)).groupByKey().reduce((v1, v2) -> {
            return "{\"amount\":\"" + (get_amount(v1) + get_amount(v2)) + "\",\"currency\":\"EUR\"}";
        });

        managerCredits.mapValues((k,v) -> "{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"double\",\"optional\":false,\"field\":\"amount\"},{\"type\":\"string\",\"optional\":false,\"field\":\"client_email\"}],\"optional\":false},\"payload\":{\"amount\":" + get_amount(v) + ",\"client_email\":\"" + k + "\"}}"
        ).toStream().to("manager_revenue");


        org.apache.kafka.streams.KafkaStreams streams = new org.apache.kafka.streams.KafkaStreams(builder.build(), props);


        streams.cleanUp();
        streams.start();

        consumer = new ListConsumer();
        consumer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                streams.close();
                consumer.close();
            }
        }));

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
        //System.out.println("AQUI->"+record);
        String email = consumer.getManager(record);
        return email;
    }

}