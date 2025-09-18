package dev.anil.selfauthentication.Service;

import dev.anil.selfauthentication.Exceptions.InvalidCredentialsException;
import dev.anil.selfauthentication.Exceptions.InvalidTokenException;
import dev.anil.selfauthentication.Exceptions.UserNotFoundException;
import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;
import dev.anil.selfauthentication.Repositories.TokenRepositoty;
import dev.anil.selfauthentication.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenRepositoty tokenRepositoty;
    private final SecretKey secretKey;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepositoty tokenRepositoty,
                           SecretKey secretKey) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepositoty = tokenRepositoty;
        this.secretKey = secretKey;
    }

    @Override
    public User signUp(String username, String email, String password) {

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        User user = new User();
        user.setEmail(email);
        user.setName(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) throws InvalidCredentialsException {

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            if(bCryptPasswordEncoder.matches(password, userOptional.get().getPassword()))
            {
                User user = userOptional.get();

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("name", user.getName());
                claims.put("iss", "scaler.com");
                claims.put("sub", user.getEmail());
                claims.put("exp", Instant.now().plus(30, ChronoUnit.DAYS).getEpochSecond());
                claims.put("iat", Instant.now().getEpochSecond());

                String jwt = Jwts.builder().claims(claims).signWith(secretKey).compact();

                Token token = new Token();
                token.setExpiryDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)));
                tokenRepositoty.save(token);

                List<Token> tokens = new ArrayList<>();
                tokens.add(token);
                user.setTokens(tokens);
                userRepository.save(user);
                return jwt;
            }
            else
                throw new InvalidCredentialsException("Invalid Credenitals");
        }
        else {
            // redirect to the login page
            return null;
        }
    }

    @Override
    public Claims validate(String tokenValue) throws InvalidTokenException {
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = parser.parseSignedClaims(tokenValue).getPayload();

        long date = (Long)claims.get("exp");
        if(date < Instant.now().getEpochSecond())
            throw new InvalidTokenException("Invalid Token");

        return claims;
    }


    @Override
    public void logout(String tokenValue) {

        Optional<Token> tokenOptional = tokenRepositoty.findBytokenValue(tokenValue);
            if (tokenOptional.isPresent()) {

                    Token token = tokenOptional.get();
                    token.setExpiryDate(Date.from(Instant.now()));
                    tokenRepositoty.save(token);
            }
    }
}
