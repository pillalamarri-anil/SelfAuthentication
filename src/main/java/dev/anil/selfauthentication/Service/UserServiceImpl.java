package dev.anil.selfauthentication.Service;

import dev.anil.selfauthentication.Exceptions.InvalidCredentialsException;
import dev.anil.selfauthentication.Exceptions.InvalidTokenException;
import dev.anil.selfauthentication.Exceptions.UserNotFoundException;
import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;
import dev.anil.selfauthentication.Repositories.TokenRepositoty;
import dev.anil.selfauthentication.Repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenRepositoty tokenRepositoty;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepositoty tokenRepositoty) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepositoty = tokenRepositoty;
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
    public Token login(String email, String password) throws InvalidCredentialsException {

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            if(bCryptPasswordEncoder.matches(password, userOptional.get().getPassword()))
            {
                User user = userOptional.get();

                Token token = new Token();

                String tokenValue = RandomStringUtils.randomAlphanumeric(128);
                token.setTokenValue(tokenValue);
                token.setUser(user);

                Instant now = Instant.now();
                now.plus(30, ChronoUnit.DAYS);
                token.setExpiryDate(Date.from(now));
                tokenRepositoty.save(token);


                List<Token> tokens = new ArrayList<>();
                tokens.add(token);
                user.setTokens(tokens);
                userRepository.save(user);
                return token;
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
    public User validate(String tokenValue) throws InvalidTokenException {

        //tokenRepositoty.existsBytokenValueAndExpiryDateBefore(tokenValue, new Date()))

        Optional<Token> tokenOptional = tokenRepositoty.findBytokenValue(tokenValue);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            if (token.getTokenValue().equals(tokenValue) && token.getExpiryDate().after(new Date())) {
                return token.getUser();
            }
        }
        throw new InvalidTokenException("Invalid Token");
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
