package dev.anil.selfauthentication.DTO;

import dev.anil.selfauthentication.Models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private String username;
    private String email;
    private List<String> roles;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getName());

        return userDTO;
    }
}
