package kafka;
import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Random;
public class Client {



    public static void main(String[] args) throws Exception{
        Random rand = new Random();

//Assign topicName to string variable
        String creditsTopic = args[0].toString();
        String paymentsTopic = args[1].toString();
// create instance for properties to access producer configs
        Properties props = new Properties();
//Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");
//Set acknowledgements for producer requests.
        props.put("acks", "all");
//If the request fails, the producer can automatically retry,
        props.put("retries", 0);
//Specify buffer size in config
        props.put("batch.size", 16384);
//Reduce the no of requests less than 0
        props.put("linger.ms", 1);
//The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        Producer<String, Long> producer = new KafkaProducer<>(props);

        while(true){
            // Generate random integers in range 0 to 999
            int credit = rand.nextInt(1000);
            int payment = rand.nextInt(1000);

            //producer.send(new ProducerRecord<String, Long>(creditsTopic, Integer.toString(credit), (long) credit));
            //System.out.println("Sending message " + credit + " to topic " + creditsTopic);
            producer.send(new ProducerRecord<String, Long>(paymentsTopic, Integer.toString(payment), (long) payment));
            System.out.println("Sending message " + payment + " to topic " + paymentsTopic);

            Thread.sleep(10000);
        }

    }

}