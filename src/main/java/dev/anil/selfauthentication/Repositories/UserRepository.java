package dev.anil.selfauthentication.Repositories;

import dev.anil.selfauthentication.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUserByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmail(String email);
}
