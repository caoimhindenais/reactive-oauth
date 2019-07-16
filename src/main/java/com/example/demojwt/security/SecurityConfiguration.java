package com.example.demojwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.security.KeyPair;

@Configuration
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Value("${basic.auth.username}")
    String username;

    @Value("${basic.auth.password}")
    String password;

    @Value("${basic.auth.role}")
    String role;

    @Value("${key.pair.alias}")
    String keyPairAlias;

    @Value("${keystore.password}")
    String keystorePassword;

    @Value("${keystore.name}")
    String keystoreName;

    @Autowired
    JWTAuthenticationConverter JWTAuthenticationConverter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                //.httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
                .authorizeExchange()
                    .pathMatchers("/token")
                    .authenticated().and().httpBasic()
                //.permitAll()
                .and()
                    .authorizeExchange()
                    .pathMatchers("/flip")
                    .authenticated().and().addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter(){
        AuthenticationWebFilter bearerAuthenticationFilter;
        ReactiveAuthenticationManager authManager;

        authManager  = new JWTTokenAuthenticationManager();
        bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);

        bearerAuthenticationFilter.setServerAuthenticationConverter(JWTAuthenticationConverter::apply);
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/flip"));

        return bearerAuthenticationFilter;
    }

    @Bean
    ReactiveUserDetailsService userDetailService(PasswordEncoder encoder) {
        User.UserBuilder user = User.withUsername(username)
                .roles(role)
                .password(encoder.encode(password));
        return new MapReactiveUserDetailsService(user.build());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystoreName), keystorePassword.toCharArray());
        return keyStoreKeyFactory.getKeyPair(keyPairAlias);
    }
}