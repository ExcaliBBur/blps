package com.example.confirmator.service;

import com.example.confirmator.api.repository.ProcessedRepository;
import com.example.confirmator.config.kafka.KafkaProperties;
import com.example.confirmator.dto.kafka.confirmation.ConfirmationAnswer;
import com.example.confirmator.dto.kafka.confirmation.ConfirmationRequest;
import com.example.confirmator.model.entity.Processed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private static final Long TIMEOUT = 100L;

    private final Consumer<String, ConfirmationRequest> consumer;
    private final Producer<String, ConfirmationAnswer> producer;

    private final KafkaProperties kafkaProperties;

    private final TransactionTemplate transactionTemplate;
    private final ProcessedRepository processedRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void process() {
        try (consumer; producer) {
            consumer.subscribe(List.of(kafkaProperties.getInboundTopic()));

            while (true) {
                var consumerRecords = consumer.poll(Duration.ofMillis(TIMEOUT));

                try {
                    for (var consumerRecord : consumerRecords) {
                        transactionTemplate.executeWithoutResult(status -> {
                            var reservation = consumerRecord.value().getConfirmationId();
                            if (processedRepository.existsByRequest(reservation)) {
                                return;
                            }
                            processedRepository.save(new Processed(null, reservation));

                            var producerRecord = new ProducerRecord<String, ConfirmationAnswer>(
                                    kafkaProperties.getOutboundTopic(),
                                    new ConfirmationAnswer(reservation, true)
                            );
                            producer.send(producerRecord);
                        });
                    }

                    consumer.commitSync();
                } catch (KafkaException e) {
                    log.error("Ошибка при отправке сообщения в Kafka: {}", e.getMessage(), e);
                }
            }
        }
    }

}
