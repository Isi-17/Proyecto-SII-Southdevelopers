package com.uma.southdevelopers.repositories;

import java.util.Optional;

import com.uma.southdevelopers.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    void deleteById(Long userId);

    boolean existsById(Long userId);
}
