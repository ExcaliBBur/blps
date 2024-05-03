package com.example.lab.service;

import com.example.lab.dto.rest.jwt.ResponseJwt;
import com.example.lab.exception.IllegalAccessException;
import com.example.lab.model.entity.User;
import com.example.lab.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final DetailsService detailsService;
    private final JwtUtils jwtUtils;

    public Mono<ResponseJwt> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return detailsService.createUser(user)
                .flatMap(this::generateAccessToken)
                .map(ResponseJwt::new);
    }

    public Mono<ResponseJwt> authenticate(User auth) {
        return detailsService.findByUsername(auth.getUsername())
                .filter(user -> passwordEncoder.matches(auth.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.error(new IllegalAccessException("Несовпадение паролей")))
                .flatMap(user -> generateAccessToken(user).map(ResponseJwt::new));
    }

    private Mono<String> generateAccessToken(UserDetails user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    private Mono<String> generateAccessToken(Map<String, Object> extraClaims, UserDetails user) {
        return jwtUtils.generateAccessToken(extraClaims, user);
    }

}
