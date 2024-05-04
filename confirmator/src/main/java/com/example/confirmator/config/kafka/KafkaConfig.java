package com.example.confirmator.config.kafka;

import com.example.confirmator.dto.kafka.confirmation.ConfirmationAnswer;
import com.example.confirmator.dto.kafka.confirmation.ConfirmationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public Producer<String, ConfirmationAnswer> producer() {
        return new KafkaProducer<>(producerProps());
    }

    @Bean
    public Consumer<String, ConfirmationRequest> consumer() {
        return new KafkaConsumer<>(consumerProps());
    }

    @Bean
    public Admin admin() {
        return Admin.create(adminProps());
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void outboundTopic() {
        try (var admin = admin()) {
            var newTopic = new NewTopic(kafkaProperties.getOutboundTopic(), kafkaProperties.getPartitionsNumber(),
                    kafkaProperties.getReplicationFactor());

            var config = new HashMap<String, String>();
            config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, kafkaProperties.getIsr().toString());
            newTopic.configs(config);

            var creationResult = admin.createTopics(List.of(newTopic));
            creationResult.all().get();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Невозможно создать топик, возникла ошибка: {}", e.getMessage(), e);
        }
    }

    private Properties producerProps() {
        var props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put(ProducerConfig.ACKS_CONFIG, "all");

        props.put(ProducerConfig.RETRIES_CONFIG, 1);

        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, kafkaProperties.getTransactionalId());

        return props;
    }

    private Properties consumerProps() {
        var props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroup());

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.confirmator.dto.kafka.confirmation.ConfirmationRequest");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);

        return props;
    }

    private Properties adminProps() {
        var props = new Properties();

        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

        return props;
    }

}
