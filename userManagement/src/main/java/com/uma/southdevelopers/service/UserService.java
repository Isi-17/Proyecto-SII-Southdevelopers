package com.uma.southdevelopers.service;

import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.repositories.UserRepository;
import com.uma.southdevelopers.service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email){ return  userRepository.findByEmail(email);}

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long userId, User user) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setRol(user.getRol());
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException();
        }
    }

    public User resetPassword(String email){
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if(!maybeUser.isPresent()){
           throw new UserNotFoundException();
        }else{
            User user = maybeUser.get();
            user.setPassword("nuevaAleatoria");
            return userRepository.save(user);
        }
    }

    public void deleteUser(Long userId) {
        if(userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        }else{
            throw new UserNotFoundException();
        }
    }    
}
