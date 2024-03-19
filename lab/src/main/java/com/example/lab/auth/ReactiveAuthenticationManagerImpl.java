package com.example.lab.auth;

import com.example.lab.exception.IllegalAccessException;
import com.example.lab.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveAuthenticationManagerImpl implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return userDetailsService.findByUsername(user.getUsername())
                .filter(UserDetails::isEnabled)
                .switchIfEmpty(Mono.error(new IllegalAccessException("Пользователь заблокирован или удалён")))
                .map(u -> authentication);
    }
}
