package com.example.lab.config;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    @Bean
    public Scheduler scheduler(Trigger trigger, JobDetail jobDetail, SchedulerFactoryBean factoryBean)
            throws SchedulerException {
        factoryBean.setWaitForJobsToCompleteOnShutdown(true);
        Scheduler scheduler = factoryBean.getScheduler();

        if (scheduler.checkExists(jobDetail.getKey())) {
            scheduler.deleteJob(jobDetail.getKey());
        }
        scheduler.scheduleJob(jobDetail, trigger);

        scheduler.start();
        return scheduler;
    }
}
