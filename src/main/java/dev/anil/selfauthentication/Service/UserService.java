package dev.anil.selfauthentication.Service;

import dev.anil.selfauthentication.Exceptions.InvalidCredentialsException;
import dev.anil.selfauthentication.Exceptions.InvalidTokenException;
import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;

public interface UserService {

    User signUp(String username, String email, String password);

    Token login(String email, String password) throws InvalidCredentialsException;

    User validate(String token) throws InvalidTokenException;

    void logout(String email);
}
