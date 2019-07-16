package com.example.demojwt.random;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
public class CoinFlipController {

    @Autowired
    CoinFlipService coinFlipService;

    @PreAuthorize("hasAuthority('flip')")
    @RequestMapping("/flip")
    public Mono<String> random() {
        log.info("Flipping the coin, good luck!");
        return coinFlipService.flip() ;
    }
}
