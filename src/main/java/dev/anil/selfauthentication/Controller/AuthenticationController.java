package dev.anil.selfauthentication.Controller;

import dev.anil.selfauthentication.DTO.SignInRequestDTO;
import dev.anil.selfauthentication.DTO.SignUpRequestDTO;
import dev.anil.selfauthentication.DTO.TokenDTO;
import dev.anil.selfauthentication.DTO.UserDTO;
import dev.anil.selfauthentication.Exceptions.InvalidCredentialsException;
import dev.anil.selfauthentication.Exceptions.InvalidTokenException;
import dev.anil.selfauthentication.Models.Token;
import dev.anil.selfauthentication.Models.User;
import dev.anil.selfauthentication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody SignUpRequestDTO requestDTO) {

        User user = userService.signUp(requestDTO.getUsername(), requestDTO.getEmail(), requestDTO.getPassword());
        return UserDTO.from(user);
    }

    @PostMapping("/signin")
    public TokenDTO signIn(@RequestBody SignInRequestDTO requestDTO) throws InvalidCredentialsException {

        Token token = userService.login(requestDTO.getEmail(), requestDTO.getPassword());
        return TokenDTO.from(token);
    }

    @GetMapping("/validate/{token}")
    public UserDTO validate(@PathVariable String token) throws InvalidCredentialsException, InvalidTokenException {

        User user = userService.validate(token);
        return UserDTO.from(user);
    }

    @PutMapping("/logout/{token}")
    public void logOut(@PathVariable String token) {
        userService.logout(token);
    }




}
