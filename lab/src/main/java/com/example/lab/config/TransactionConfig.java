package com.example.lab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {
//    @Bean
//    public JtaTransactionManager transactionManager() {
//        return new JtaTransactionManager();
//    }
}
