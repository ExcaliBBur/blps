package com.example.lab.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.Map;

@Configuration
@EnableR2dbcAuditing
@EnableTransactionManagement
public class ApplicationConfig {

    @Bean
    public Map<String, String> violationsMap() {
        return Map.of(
                "reservation_ticket_id_key", "Бронь на этот билет уже существует",
                "ticket_unique", "Такой билет уже существует",
                "route_unique", "Такой маршрут уже существует",
                "fk_ticket_route", "Такого маршрута не существует",
                "user_unique", "Пользователь с таким логином уже существует"
        );
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager reactiveTransactionManager) {
        return TransactionalOperator.create(reactiveTransactionManager);
    }

}
