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
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

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
            producer.initTransactions();

            while (true) {
                var consumerRecords = consumer.poll(Duration.ofMillis(TIMEOUT));
                var latch = new CountDownLatch(consumerRecords.count());
                var isFailed = new AtomicBoolean(false);

                try {
                    transactionTemplate.executeWithoutResult(status -> {
                        producer.beginTransaction();

                        for (var consumerRecord : consumerRecords) {
                            var confirmation = consumerRecord.value().getConfirmationId();
                            if (processedRepository.existsByRequest(confirmation)) {
                                continue;
                            }
                            processedRepository.save(new Processed(null, confirmation));

                            var producerRecord = new ProducerRecord<String, ConfirmationAnswer>(
                                    kafkaProperties.getOutboundTopic(),
                                    new ConfirmationAnswer(confirmation, true)
                            );
                            producer.send(producerRecord, (recordMetadata, exception) -> {
                                latch.countDown();

                                if (Objects.nonNull(exception)) {
                                    isFailed.set(true);
                                }
                            });
                        }

                        try {
                            latch.await();
                            if (isFailed.get()) {
                                producer.abortTransaction();

                                throw new KafkaException("Ошибка при отправке сообщения в Kafka");
                            }

                            producer.commitTransaction();
                            consumer.commitSync();
                        } catch (InterruptedException e) {
                            producer.abortTransaction();

                            Thread.currentThread().interrupt();
                        }
                    });
                } catch (Exception e) {
                    log.error("Невозможно обработать подтверждения, возникла ошибка: {}", e.getMessage(), e);
                }
            }
        }
    }

}
