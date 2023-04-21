package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.dto.*;
import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.security.JwtUtil;
import com.uma.southdevelopers.security.PasswordUtils;
import com.uma.southdevelopers.service.UserService;
import com.uma.southdevelopers.service.exceptions.WrongCredentialsException;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value(value="${local.server.port}")
    private int port;

    @Value(value="${local.server.host}")
    private String host;

    private String uri(String scheme, String host, int port, String ...paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for (String path: paths) {
            ub = ub.path(path);
        }
        return ub.build().toString();
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> UserDTO.fromUser(user)).toList();
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
    

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDto) {
        if(userService.existUserWithEmail(userDto.getEmail())){
            return ResponseEntity.status(409).body("Hay un usuario con el mismo correo electrónico en el sistema");
        }
        User user = userDto.user();
        String password = PasswordUtils.createPassword();
        user.setPassword(password);
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(uri("http", host, port, "/usuarios/"+savedUser.getUserId()));
    }

    @PostMapping("/passwordreset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordresetDTO pssDTO){
        Optional<String> newPassword = userService.resetPassword(pssDTO.getEmail());
        if(newPassword.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        // TODO: enviar contraseña por correo
        return  ResponseEntity.ok(newPassword.get()); // TODO: devolvemos contraseña para las pruebas, quitar.
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
    public ResponseEntity<RespuestaTokenDTO> logUser(@RequestBody LoginDTO login) {
        if(!userService.correctPassword(login.getEmail(), login.getPassword())){
            throw new WrongCredentialsException();
        }
        String token = jwtUtil.generateToken(userService.loadUserByUsername(login.getEmail()));
        return ResponseEntity.ok(new RespuestaTokenDTO(token));
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<?> badCredentials(){
        return ResponseEntity.status(403).body("Credenciales no correctas");
    }
}




