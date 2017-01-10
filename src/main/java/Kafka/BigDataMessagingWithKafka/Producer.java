package Kafka.BigDataMessagingWithKafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

// http://www.javaworld.com/article/3060078/big-data/big-data-messaging-with-kafka-part-1.html
// cd /Users/rishabhg/Downloads/devSoft/kafka/kafka_2.11-0.9.0.0
// bin/zookeeper-server-start.sh config/zookeeper.properties
// bin/kafka-server-start.sh config/server.properties
// bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic javaworld
// bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic javaworld --from-beginning
// bin/kafka-console-producer.sh --broker-list localhost:9092 --topic javaworld

public class Producer {
    private static Scanner in;
    public static void main(String[] args)throws Exception {
        String topicName = "javaworld";
        in = new Scanner(System.in);
        System.out.println("Enter message(type exit to quit)");

        //Configure the Producer
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);
        String line = in.nextLine();
        while(!line.equals("exit")) {
            ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, line);
            producer.send(rec);
            line = in.nextLine();
        }
        in.close();
        producer.close();
    }
}