package com.example.demojwt.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class TokenController {

    @Autowired
    JWTService jwtService;

    @PreAuthorize("hasRole('token')")
    @RequestMapping("/token")
    public Mono<String> token(@RequestParam String subject, @RequestParam String roles) {
        log.info("Creating token for {} ", subject);

        String token = jwtService.builder().subject(subject).roles(roles).build();

        log.info("Token " + token);

        return Mono.just(String.valueOf(token ));
    }
}
