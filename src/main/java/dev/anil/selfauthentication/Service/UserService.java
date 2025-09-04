package dev.anil.selfauthentication.Service;

import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;

public interface UserService {

    User signUp(String username, String email, String password);

    Token login(String email, String password);

    boolean validate(String token);

    void logout(String email);
}
