package com.example.lab.utils.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.access.key}")
    private String access;
    @Value("${jwt.secret.access.expiration}")
    private Long accessExpiration;

    public Mono<String> generateAccessToken(Map<String, Object> extraClaims, UserDetails details) {
        return getSignInKey(access)
                .zipWith(calculateDate(accessExpiration))
                .map(tuple -> Jwts.builder()
                        .setClaims(extraClaims)
                        .setSubject(details.getUsername())
                        .setExpiration(tuple.getT2())
                        .signWith(tuple.getT1(), SignatureAlgorithm.HS256)
                        .compact());
    }

    private Mono<Date> calculateDate(Long expiration) {
        return Mono.defer(() -> {
            LocalDateTime now = LocalDateTime.now();
            Instant expirationInstant = now.plusSeconds(expiration)
                    .atZone(ZoneId.systemDefault()).toInstant();
            return Mono.just(Date.from(expirationInstant));
        });
    }

    public Mono<Key> getSignInKey(String key) {
        return Mono.defer(() -> {
            byte[] keyBytes = Decoders.BASE64.decode(key);
            return Mono.just(Keys.hmacShaKeyFor(keyBytes));
        });
    }

    public Mono<String> extractAccessUsername(String token) {
        return extractUsername(token, access);
    }

    private Mono<String> extractUsername(String token, String key) {
        return extractClaim(token, key, Claims::getSubject);
    }

    public <T> Mono<T> extractClaim(String token, String key, Function<Claims, T> claimsResolver) {
        return extractAllClaims(token, key)
                .flatMap(c -> {
                    T t = claimsResolver.apply(c);
                    return Mono.just(t);
                });
    }

    public Mono<Claims> extractAllClaims(String token, String key) {
        return getSignInKey(key)
                .flatMap(k -> {
                    Claims claims = Jwts
                            .parserBuilder()
                            .setSigningKey(k)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
                    return Mono.just(claims);
                });
    }

    public Mono<Boolean> isAccessTokenValid(String token) {
        return isTokenValid(token, access);
    }

    private Mono<Boolean> isTokenValid(String token, String key) {
        return getSignInKey(key)
                .flatMap(k -> {
                    try {
                        Jwts.parserBuilder()
                                .setSigningKey(k)
                                .build()
                                .parseClaimsJws(token);
                        return Mono.just(true);
                    } catch (ExpiredJwtException expEx) {
                        log.error("token expired", expEx);
                    } catch (UnsupportedJwtException unsEx) {
                        log.error("unsupported jwt", unsEx);
                    } catch (MalformedJwtException mjEx) {
                        log.error("malformed jwt", mjEx);
                    } catch (Exception e) {
                        log.error("invalid token", e);
                    }
                    return Mono.just(false);
                });
    }

}
