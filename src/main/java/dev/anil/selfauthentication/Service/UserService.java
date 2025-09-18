package dev.anil.selfauthentication.Service;

import dev.anil.selfauthentication.Exceptions.InvalidCredentialsException;
import dev.anil.selfauthentication.Exceptions.InvalidTokenException;
import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;
import io.jsonwebtoken.Claims;

public interface UserService {

    User signUp(String username, String email, String password);

    String login(String email, String password) throws InvalidCredentialsException;

    Claims validate(String token) throws InvalidTokenException;

    void logout(String email);
}
