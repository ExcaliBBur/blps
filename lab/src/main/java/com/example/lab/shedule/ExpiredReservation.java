package com.example.lab.shedule;

import com.example.lab.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExpiredReservation extends QuartzJobBean {
    private final ReservationRepository reservationRepository;

    @Value("${clean-up-reservation-delay-seconds}")
    private Integer time_delay_seconds;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("Started delete with time_delay: " + time_delay_seconds);
        reservationRepository.deleteExpired(time_delay_seconds).block();
    }
}
