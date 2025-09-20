package dev.anil.selfauthentication.OAuth2;

import dev.anil.selfauthentication.Models.User;
import dev.anil.selfauthentication.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // for this application, email is the unique identifier of the user, not userid

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            UserDetails userDetails = new CustomUserDetails(user.get());
            return userDetails;

        }
        throw new UsernameNotFoundException(email);
    }
}
