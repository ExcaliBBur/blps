package com.example.lab.delegates;

import com.example.lab.dto.mapper.RouteMapper;
import com.example.lab.dto.route.CreateRouteRequest;
import com.example.lab.model.entity.Route;
import com.example.lab.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Named("createRoute")
@RequiredArgsConstructor
public class CreateRoute implements JavaDelegate {
    private final RouteService routeService;
    private final RouteMapper routeMapper;
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String departure = (String) delegateExecution.getVariable("departure");
        String source = (String) delegateExecution.getVariable("source");
        String destination = (String) delegateExecution.getVariable("destination");
        Long trainNumber = (Long) delegateExecution.getVariable("train_number");

        CreateRouteRequest dto = new CreateRouteRequest(LocalDate.parse(departure, dtf), trainNumber, source, destination);
        routeService.createRoute(routeMapper.mapToRoute(dto));
    }
}
