package dev.anil.selfauthentication.configs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

@Configuration
public class bcryptEncoderConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();
        httpSecurity.cors().disable();
        httpSecurity.authorizeHttpRequests(authorizeRequests ->
        authorizeRequests.anyRequest().permitAll());
        return httpSecurity.build();
    }

    @Bean
    public SecretKey getSecretKey() {
        MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
        return macAlgorithm.key().build();
    }

}
