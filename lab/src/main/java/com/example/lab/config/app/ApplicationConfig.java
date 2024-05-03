package com.example.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
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
}
