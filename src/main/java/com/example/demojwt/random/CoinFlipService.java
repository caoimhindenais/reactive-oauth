package com.example.demojwt.random;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class CoinFlipService {

    Mono<String> flip() {
        Random random = new Random();
        return Mono.justOrEmpty(random.nextBoolean() ? "heads":"tails");
    }
}
