package com.example.demojwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class AuthenticationBuilder {

    static Mono<Authentication> create(String token) {
        String subject;
        String auths;
        List authorities;

        DecodedJWT jwt = JWT.decode(token);
        subject =  jwt.getSubject();
        auths =  jwt.getClaims().get("roles").asString();

        authorities = Stream.of(auths.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            return  Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, null, authorities));
    }
}
