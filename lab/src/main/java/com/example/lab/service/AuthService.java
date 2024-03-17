package com.example.lab.service;

import com.example.lab.dto.jwt.ResponseJwt;
import com.example.lab.model.entity.User;
import com.example.lab.model.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import utils.jwt.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DetailsService detailsService;
    private final JwtUtils jwtUtils;

    public Mono<ResponseJwt> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return detailsService.createUser(user)
                .flatMap(this::generateAccessToken)
                .map(ResponseJwt::new);
    }

    public Mono<ResponseJwt> authenticate(User user) {
        return Mono.fromCallable(() -> authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
                ))
                .flatMap(authentication -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    return generateAccessToken(userDetails)
                            .map(ResponseJwt::new);
                });
    }

    private Mono<String> generateAccessToken(UserDetails user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    private Mono<String> generateAccessToken(Map<String, Object> extraClaims, UserDetails user) {
        return jwtUtils.generateAccessToken(extraClaims, user);
    }

}
