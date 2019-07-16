package com.example.demojwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;


@Slf4j
public class JWTServiceTest {



    @Test
    public void testverifyTokenSignature(){
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "secret".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("token");

        String token = "eyJhbGciOiJSUzI1NiJ9.eyJyb2xlcyI6ImZsaXAiLCJleHAiOjE1NjM1Mjg3NDZ9.g2XLgicVJnXr2GrwoZuSnxpYOfUD8g_H7K0QWlMzD14OfBw1fA9edG6Ug5fCdwTzo0qd-2Yr8w4mdhABwP2XFLS3zMwxXMK4YltKsK7PDg395cdciCrLsn5a6ogjtjxQ2DOFMjUVdtDx2TNU0_txbau5Ger8Q2EWIi2Gb8KG3PQitLG6kcOSA8SRQWN0pwpLSupKCIfNF5H25xV4beXHAK_LzioCSx4GP77CDgOf--w7vycExrVSNJ27JgBKnyTpMib-fyNxv8B1qB-fOIRBB4d5QKS_kKiJkFDrufkuh8tKUo2nvcufR6KxRf4ItqQ0foeMwWDiJPuVkBGl3XYfnw";
        DecodedJWT decodedJWT = JWT.decode(token);
        log.info("Header {}", decodedJWT.getHeader());
        log.info("Payload {}", decodedJWT.getPayload());
        log.info("Subject {}", decodedJWT.getSubject());
        log.info("Signature {}",decodedJWT.getSignature());

        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), null);
        algorithm.verify(decodedJWT);

    }
}
