package dev.anil.selfauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SelfAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfAuthenticationApplication.class, args);
    }

}
