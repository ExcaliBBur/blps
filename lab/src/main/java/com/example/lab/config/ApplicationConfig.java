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
                "_user_pkey", "Пользователь с таким логином уже существует"
        );
    }
}
