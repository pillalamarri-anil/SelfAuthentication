package dev.anil.selfauthentication.Repositories;

import dev.anil.selfauthentication.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepositoty extends JpaRepository<Token, Long> {

    Optional<Token> findBytokenValue(String token);

    Token save(Token token);

    boolean existsBytokenValueAndExpiryDateBefore(String tokenValue, Date expiryDate);

}
