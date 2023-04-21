package com.uma.southdevelopers.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.uma.southdevelopers.dto.RespuestaTokenDTO;
import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.repositories.UserRepository;
import com.uma.southdevelopers.security.PasswordUtils;
import com.uma.southdevelopers.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email){ return  userRepository.findByEmail(email);}

    public boolean existUserWithEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long userId, User user) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            user.setUserId(existingUser.getUserId());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    public Optional<String> resetPassword(String email){
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if(maybeUser.isEmpty()){
            return null;
        }
        User user = maybeUser.get();
        String newPassword = PasswordUtils.createPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return Optional.of(newPassword);
    }

    public void deleteUser(Long userId) {
        if(userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        }else{
            throw new UserNotFoundException();
        }
    }

    public boolean correctPassword(String email, String password) {
        Optional<User> maybyUser = userRepository.findByEmail(email);
        return maybyUser.isPresent() && passwordEncoder.matches(password, maybyUser.get().getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<SimpleGrantedAuthority> auths = new java.util.HashSet<>();
        Set<User.Role> roles = user.getRoles();
        roles.forEach(rol -> auths.add(new SimpleGrantedAuthority("ROLE_" + rol.toString())));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                auths);
    }
}
