package t1.homework.audit;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaAuditSender implements AuditSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaAuditSender(String bootstrapServers, String topic) {
        this.topic = topic;
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @Override
    public void send(String message) {
        kafkaTemplate.send(new ProducerRecord<>(topic, message));
    }
}
