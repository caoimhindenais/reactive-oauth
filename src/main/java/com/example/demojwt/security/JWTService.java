package com.example.demojwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;


@Slf4j
@Service
public class JWTService {


    public static final int THREE_DAYS_IN_MILLISECONDS = 259200000;


    @Autowired
    KeyPair keyPair;

    @Builder(builderMethodName = "builder")
    public String createJWT(String subject, String roles) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        HashMap map = new HashMap<String, String>();
        map.put("roles", roles);

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer("Token Contoller Issuer")
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis()+ THREE_DAYS_IN_MILLISECONDS))
                .signWith( keyPair.getPrivate(),SignatureAlgorithm.RS256);

        return builder.compact();

    }

    Mono<String> check(String token) {
        return Mono.justOrEmpty((token))
                .filter(isNotExpired)
                .filter(validSignature);
    }


    private Predicate<String> isNotExpired = token -> JWT.decode(token).getExpiresAt().after(Date.from(Instant.now().minus(Duration.ofDays(1))));


    private Predicate<String> validSignature = token -> {

        DecodedJWT jwt = JWT.decode(token);

        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), null);
        algorithm.verify(jwt);
        return true;
    };
}
