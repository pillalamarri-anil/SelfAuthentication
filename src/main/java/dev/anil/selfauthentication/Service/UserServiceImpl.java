package dev.anil.selfauthentication.Service;

import dev.anil.selfauthentication.Exceptions.InvalidCredentialsException;
import dev.anil.selfauthentication.Exceptions.UserNotFoundException;
import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;
import dev.anil.selfauthentication.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
    public Token login(String email, String password) {

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            if(bCryptPasswordEncoder.matches(password, userOptional.get().getPassword()))
            {
                Token token = new Token();
                token.setTokenValue(userOptional.get().getPassword());
                Instant now = Instant.now();
                now.plus(30, ChronoUnit.DAYS);
                token.setExpiryDate(Date.from(now));

                User user = userOptional.get();
                List<Token> tokens = new ArrayList<>();
                tokens.add(token);
                user.setTokens(tokens);
                userRepository.save(user);
                return token;
            }
            else
                throw new InvalidCredentialsException("Invalid Credenitals");
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public boolean validate(String token) {
        return false;
    }

    @Override
    public void logout(String email) {

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setTokens(null);
            userRepository.save(user);
        }
    }
}
