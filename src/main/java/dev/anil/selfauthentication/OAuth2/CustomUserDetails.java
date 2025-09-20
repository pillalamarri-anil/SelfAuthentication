package dev.anil.selfauthentication.OAuth2;

import dev.anil.selfauthentication.Models.Role;
import dev.anil.selfauthentication.Models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : user.getRoles())
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.getRoleName();
                }
            });

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
