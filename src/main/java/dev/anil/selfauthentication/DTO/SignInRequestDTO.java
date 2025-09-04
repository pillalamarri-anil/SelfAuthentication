package dev.anil.selfauthentication.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {

    private String email;
    private String password;
}
