package com.example.lab.service;

import com.example.lab.config.kafka.KafkaProperties;
import com.example.lab.dto.kafka.confirmation.ConfirmationAnswer;
import com.example.lab.dto.kafka.confirmation.ConfirmationRequest;
import com.example.lab.exception.AlreadyPaidException;
import com.example.lab.exception.EntityNotFoundException;
import com.example.lab.exception.IllegalAccessException;
import com.example.lab.model.entity.Confirmation;
import com.example.lab.model.entity.Reservation;
import com.example.lab.model.entity.User;
import com.example.lab.model.enumeration.PrivilegeEnum;
import com.example.lab.repository.ConfirmationRepository;
import com.example.lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConfirmationRepository confirmationRepository;

    private final TransactionalOperator transactionalOperator;

    private final KafkaSender<String, ConfirmationRequest> kafkaSender;
    private final KafkaReceiver<String, ConfirmationAnswer> kafkaReceiver;
    private final KafkaProperties kafkaProperties;

    public Mono<Reservation> createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Mono<Void> deleteReservation(Long route, Integer seat) {
        return getReservationByRouteAndSeat(route, seat)
                .flatMap(reservationRepository::delete);
    }

    @Transactional
    public Mono<Void> buyReservation(Long route, Integer seat) {
        return getReservationByRouteAndSeat(route, seat)
                .filter(reservation -> !reservation.getBought())
                .switchIfEmpty(Mono.error(new AlreadyPaidException("Бронь уже оплачена")))
                .flatMap(reservation -> generateUniqueUUID()
                        .flatMap(uuid -> {
                            Confirmation confirmation = new Confirmation(null, uuid, reservation.getId(),
                                    false);
                            return confirmationRepository.save(confirmation)
                                    .thenReturn(uuid);
                        })
                        .flatMap(uuid -> {
                            ConfirmationRequest request = new ConfirmationRequest(uuid);
                            SenderRecord<String, ConfirmationRequest, String> record = SenderRecord.create(
                                    new ProducerRecord<>(kafkaProperties.getOutboundTopic(), request),
                                    null
                            );

                            return kafkaSender.send(Mono.just(record))
                                    .onErrorMap(throwable -> new KafkaException("Ошибка при отправке сообщения в Kafka",
                                            throwable))
                                    .onErrorResume(Mono::error)
                                    .then();
                        }));
    }

    private Mono<String> generateUniqueUUID() {
        return Mono.defer(() -> {
            String uuid = UUID.randomUUID().toString();
            return confirmationRepository.existsByRequestId(uuid)
                    .flatMap(exists -> {
                        if (exists) {
                            return generateUniqueUUID();
                        } else {
                            return Mono.just(uuid);
                        }
                    });
        });
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void consume() {
        kafkaReceiver.receive()
                .subscribe(record -> {
                    ReceiverOffset offset = record.receiverOffset();

                    confirmationRepository.findConfirmationByRequestId(record.value().getConfirmationId())
                            .filter(confirmation -> !confirmation.getProcessed())
                            .flatMap(confirmation -> confirmationRepository
                                    .updateConfirmationProcessedByRequestId(confirmation.getRequestId(), true))
                            .flatMap(confirmation -> reservationRepository
                                    .updateStatusById(confirmation.getReservationId(), record.value().getVerdict()))
                            .then(Mono.fromRunnable(offset::acknowledge))
                            .doOnError(throwable -> log.error("Невозможно обработать ответ, возникла ошибка: {}",
                                    throwable.getMessage(), throwable))
                            .as(transactionalOperator::transactional).subscribe();
                });
    }

    public Mono<Reservation> getReservationByRouteAndSeat(Long route, Integer seat) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class)
                .flatMap(auth -> reservationRepository.findReservationByTicket(route, seat)
                        .filter(reservation -> auth.getId().equals(reservation.getUser()) || hasEditPrivilege(auth))
                        .switchIfEmpty(Mono.error(new IllegalAccessException("Недостаточно прав для получения доступа к чужой брони")))
                        .flatMap(Mono::just)
                )
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Бронь на билет с таким id не найдена")));
    }

    private boolean hasEditPrivilege(User auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(PrivilegeEnum.RESERVATION_EDIT_PRIVILEGE.toString()));
    }

}
