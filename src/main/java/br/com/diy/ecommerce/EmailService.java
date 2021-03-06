package br.com.diy.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EmailService {

    public static void main(String[] args) {
        var isConsuming = true;
        var consumer = new KafkaConsumer<String, String>(properties());
        consumer.subscribe(Collections.singletonList("ECOMMERCE_SEND_EMAIL"));

        while(isConsuming) {
            var records = consumer.poll(Duration.ofMillis(1000));
            if (!records.isEmpty()) {
                System.out.println(records.count() + " Registro(s) encontrado(s)\n");
            }
            for (var record : records) {
                System.out.println("--------------------------------------------------");
                System.out.println("Enviando email");
                System.out.println("key: " + record.key());
                System.out.println("value: " + record.value());
                System.out.println("partition: " + record.partition());
                System.out.println("offset: " + record.offset());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Email enviado");
            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());
        return properties;
    }
}
