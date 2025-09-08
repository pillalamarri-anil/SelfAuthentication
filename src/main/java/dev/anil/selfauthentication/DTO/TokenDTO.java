package dev.anil.selfauthentication.DTO;

import dev.anil.selfauthentication.Models.Token;

import java.util.Date;
import java.util.List;


@lombok.Getter
@lombok.Setter

public class TokenDTO {
    private String tokenValue;
    private Date expiryDate;
    private String email;

    //private List<String> roles;

    public static TokenDTO from(Token token) {
        if(token == null) return null;
        TokenDTO dto = new TokenDTO();
        dto.tokenValue = token.getTokenValue();
        dto.expiryDate = token.getExpiryDate();
        dto.email = token.getUser().getEmail();
        return dto;
    }
}
