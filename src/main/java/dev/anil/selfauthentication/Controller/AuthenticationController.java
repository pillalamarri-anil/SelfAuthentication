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
import io.jsonwebtoken.Claims;
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

        System.out.println("SignUp request: " + requestDTO);
        User user = userService.signUp(requestDTO.getUsername(), requestDTO.getEmail(), requestDTO.getPassword());
        return UserDTO.from(user);
    }

    @PostMapping("/signin")
    public String signIn(@RequestBody SignInRequestDTO requestDTO) throws InvalidCredentialsException {

        return userService.login(requestDTO.getEmail(), requestDTO.getPassword());
    }

    @GetMapping("/validate/{token}")
    public Claims validate(@PathVariable String token) throws InvalidCredentialsException, InvalidTokenException {

        return userService.validate(token);
    }

    @PutMapping("/logout/{token}")
    public void logOut(@PathVariable String token) {
        userService.logout(token);
    }




}
