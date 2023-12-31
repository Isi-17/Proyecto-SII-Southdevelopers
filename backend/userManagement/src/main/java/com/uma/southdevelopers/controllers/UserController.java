package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.dto.*;
import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.security.JwtUtil;
import com.uma.southdevelopers.security.PasswordUtils;
import com.uma.southdevelopers.service.UserService;
import com.uma.southdevelopers.service.exceptions.UserNotFoundException;
import com.uma.southdevelopers.service.exceptions.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private String host = "localhost";

    private int port = 8082;  //Puerto en el que se ejecuta el microservicio de notificaciones

    private RestTemplate restTemplate = new RestTemplate();

    private URI uri(String scheme, String host, int port, String ...paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for (String path: paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> UserDTO.fromUser(user)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserById(id);
        if(optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body("El usuario no existe");
        }
        return ResponseEntity.ok(UserDTO.fromUser(optionalUser.get()));
    }
    

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDto, UriComponentsBuilder uriComponentsBuilder) {
        if(userService.existUserWithEmail(userDto.getEmail())){
            return ResponseEntity.status(409).body("Hay un usuario con el mismo correo electrónico en el sistema");
        }
        User user = userDto.user();
        String password = PasswordUtils.createPassword();
        user.setPassword(password);
        User savedUser = userService.createUser(user);
        return ResponseEntity.created(
                uriComponentsBuilder.path("/usuarios")
                .path(String.format("/%d", savedUser.getUserId()))
                .build()
                .toUri()
        ).build();
    }

    @PostMapping("/passwordreset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordresetDTO pssDTO){
        Optional<String> newPassword = userService.resetPassword(pssDTO.getEmail());
        if(newPassword.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        var notiDTO = NotificationDTO.builder()
                .emailDestino(pssDTO.getEmail())
                .cuerpo(newPassword.get())
                .asunto("Restablecer contraseña")
                .programacionEnvio(new Date().toString())
                .tipoDeNotificacion("PASSWORD_RESET")
                .medios(List.of("EMAIL"))
                .build();

        var peticion = post("http", host, port,"/notification", notiDTO);
        //var respuesta = restTemplate.exchange(peticion,Void.class); //Descomentar esta linea cuando el microservicio
                                                                    //de notificaciones este activo en el puerto port
        return  ResponseEntity.ok(newPassword.get()); // TODO: devolvemos contraseña para las pruebas, quitar.
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(id, userDTO.user());
        return new ResponseEntity<>(UserDTO.fromUser(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFound(){
        return ResponseEntity.status(404).body("El usuario no existe");
    }
}




