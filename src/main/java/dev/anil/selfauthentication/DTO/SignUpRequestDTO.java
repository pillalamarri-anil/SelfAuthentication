package dev.anil.selfauthentication.DTO;


@lombok.Getter
@lombok.Setter


public class SignUpRequestDTO {
    private String username;
    private String email;
    private String password;
}
