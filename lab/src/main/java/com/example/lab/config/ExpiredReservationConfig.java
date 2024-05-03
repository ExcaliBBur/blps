package com.example.lab.config;

import com.example.lab.shedule.ExpiredReservation;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpiredReservationConfig {
    @Bean
    public JobDetail expiredReservationJobDetail() {
        return JobBuilder.newJob(ExpiredReservation.class)
                .withIdentity("expired reservation job")
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    public Trigger expiredReservationTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(expiredReservationJobDetail())
                .withIdentity("expired reservation trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?"))
                .build();
    }

}
