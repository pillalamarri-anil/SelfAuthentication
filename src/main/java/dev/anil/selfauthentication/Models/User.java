package dev.anil.selfauthentication.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@Entity
public class User extends BaseModel {

    private String name;
    private String email;
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Token> tokens;



    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles;
}
