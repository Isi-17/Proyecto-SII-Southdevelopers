package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.dto.LoginDTO;
import com.uma.southdevelopers.dto.RespuestaTokenDTO;
import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.service.JwtService;
import com.uma.southdevelopers.service.UserService;
import com.uma.southdevelopers.service.exceptions.UserNotFoundException;
import com.uma.southdevelopers.service.exceptions.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);
        if(!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/passwordreset")
    public ResponseEntity<User> resetPassword(@RequestBody String email){
        User user = userService.resetPassword(email);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{

            return  new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if(updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaTokenDTO> deleteUser(@RequestBody LoginDTO login) {
        if(!userService.existUserByEmail(login.getEmail()) || !userService.correctPassword(login.getEmail(), login.getPassword())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        RespuestaTokenDTO respuesta = new RespuestaTokenDTO(jwtService.createToken(login.getEmail()));
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
}




