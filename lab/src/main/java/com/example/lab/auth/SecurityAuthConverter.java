package com.example.lab.auth;

import com.example.lab.service.DetailsService;
import com.example.lab.utils.jwt.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityAuthConverter implements ServerAuthenticationConverter {

    private final String BEARER = "Bearer ";
    private final DetailsService detailsService;
    private final JwtUtils jwtUtils;

    public SecurityAuthConverter(DetailsService detailsService, JwtUtils jwtUtils) {
        this.detailsService = detailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(this::extractBearerToken)
                .flatMap(token -> jwtUtils.isAccessTokenValid(token)
                        .flatMap(isValid -> isValid ? jwtUtils.extractAccessUsername(token) : Mono.empty())
                        .flatMap(detailsService::findByUsername)
                        .map(userDetails ->
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities())
                        ));
    }

    private Mono<String> extractBearerToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION))
                .flatMap(token -> {
                    if (token.startsWith(BEARER)) {
                        return Mono.just(token.substring(BEARER.length()));
                    }

                    return Mono.empty();
                });
    }
}
