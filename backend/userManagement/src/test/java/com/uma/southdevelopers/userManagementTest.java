package com.uma.southdevelopers;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.uma.southdevelopers.dto.*;
import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.repositories.UserRepository;
import com.uma.southdevelopers.security.JwtUtil;
import com.uma.southdevelopers.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Tests de userManagement")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class userManagementTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    private String host = "localhost";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

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

    private RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        return peticion;
    }

    private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.delete(uri)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> postJwt(String scheme, String host, int port, String path, T object, String jwt) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+jwt)
                .body(object);
        return peticion;
    }

    private RequestEntity<Void> getJwt(String scheme, String host, int port, String path, String jwt) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+jwt)
                .build();
        return peticion;
    }

    private RequestEntity<Void> deleteJwt(String scheme, String host, int port, String path, String jwt) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.delete(uri)
                .header("Authorization", "Bearer "+jwt)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> putJwt(String scheme, String host, int port, String path, T object, String jwt) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+jwt)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private void compruebaCampos(User user, User user2){
        assertThat(user.getUserId()).isEqualTo(user2.getUserId());
        assertThat(user.getName()).isEqualTo(user2.getName());
        assertThat(user.getSurname1()).isEqualTo(user2.getSurname1());
        assertThat(user.getSurname2()).isEqualTo(user2.getSurname2());
        assertThat(user.getEmail()).isEqualTo(user2.getEmail());
    }

    private String crearUsuarioVicerrectorado() {
        var vicerrector = User.builder().name("vicerrector")
                .password("1234").email("vice@mail.es").roles(Set.of(User.Role.VICERRECTORADO)).build();
        userRepo.save(vicerrector);
        return jwtUtil.generateToken(userService.loadUserByUsername("vice@mail.es"));
    }

    private String crearUsuarioCorrector() {
        var corrector = User.builder().name("corrector")
                .password("1234").email("corrector@mail.es").roles(Set.of(User.Role.CORRECTOR)).build();
        userRepo.save(corrector);
        return jwtUtil.generateToken(userService.loadUserByUsername("corrector@mail.es"));
    }

    @Nested
    @DisplayName("Acceso a usuario individual")
    public class AccesoUsuarioIndividual {

        @Test
        @DisplayName("Acceder a usuario existente")
        public void accederUsuarioExistente(){
            // Creamos el usuario en la base de datos
            User user = new User();
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            User savedUser = userService.createUser(user);

            var jwt = crearUsuarioVicerrectorado();

            var peticion = getJwt("http", host, port, "/usuarios/"+savedUser.getUserId(), jwt);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

            UserDTO userDTORespuesta = respuesta.getBody();

            compruebaCampos(savedUser ,userDTORespuesta.user());
        }

        @Test
        @DisplayName("Acceder a usuario sin autorización")
        public void accederUsuarioExistenteSinAutorizacion(){
            // Creamos el usuario en la base de datos
            User user = new User();
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            User savedUser = userService.createUser(user);

            var peticion = get("http", host, port, "/usuarios/"+savedUser.getUserId());

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }

        @Test
        @DisplayName("Acceder a usuario no existente")
        public void accederUsuarioNoExistente(){

            var jwt = crearUsuarioCorrector();

            var id = 100L;

            var peticion = getJwt("http", host, port, "/usuarios/"+
                    (userRepo.existsById(id)? id : id+1), jwt);
            var respuesta = restTemplate.exchange(peticion,
                    Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
    }

    @Nested
    @DisplayName("Acceso a todos los usuarios")
    public class AccesoUsuarioLista {
        @Test
        @DisplayName("Acceder a lista de usuarios con autorización")
        public void accederListaUsuarios(){
            User user1 = new User();
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");

            User user2 = new User();
            user2.setName("Pepe");
            user2.setSurname1("Garcia");
            user2.setSurname2("Garcia");
            user2.setEmail("pepegg@gmail.com");
            user2.setPassword("password");

            User savedUser1 = userService.createUser(user1);
            User savedUser2 = userService.createUser(user2);

            var jwt = crearUsuarioCorrector();

            var peticion = getJwt("http", host, port, "/usuarios",jwt);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<UserDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(3);

            compruebaCampos(user1, respuesta.getBody().get(0).user());
            compruebaCampos(user2, respuesta.getBody().get(1).user());
        }

        @Test
        @DisplayName("Acceder a lista de usuarios sin autorización")
        public void accederListaUsuariosSinAutorizacion(){
            User user1 = new User();
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");

            User user2 = new User();
            user2.setName("Pepe");
            user2.setSurname1("Garcia");
            user2.setSurname2("Garcia");
            user2.setEmail("pepegg@gmail.com");
            user2.setPassword("password");

            User savedUser1 = userService.createUser(user1);
            User savedUser2 = userService.createUser(user2);

            var peticion = get("http", host, port, "/usuarios");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<UserDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }
    }

    @Nested
    @DisplayName("Update de Usuarios")
    public class UpdateUsuario{
        @Test
        @DisplayName("Update de usuario")
        public void updateUsuario(){
            User user1 = new User();
            user1.setUserId(Long.valueOf(1));
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");

            User user2 = new User();
            user2.setUserId(Long.valueOf(2));
            user2.setName("Pepe");
            user2.setSurname1("Garcia");
            user2.setSurname2("Garcia");
            user2.setEmail("pepegg@gmail.com");
            user2.setPassword("password");

            userRepo.save(user1);
            userRepo.save(user2);

            UserDTO userDTO = UserDTO.fromUser(user1);
            userDTO.setNombre("JuanNuevo");
            userDTO.setApellido1("SanchezNuevo");
            userDTO.setApellido2("SanchezNuevo");
            userDTO.setEmail("juanssNUEVO@gmail.com");

            var jwt = crearUsuarioVicerrectorado();

            var peticion = putJwt("http", host, port,"/usuarios/1",userDTO,jwt);
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);

            compruebaCampos(respuesta.getBody().user(), userDTO.user());
            compruebaCampos(userRepo.findById(Long.valueOf(1)).get(),userDTO.user());
        }

        @Test
        @DisplayName("Update de usuario que no existe")
        public void updateUsuarioNoExiste(){
            User user1 = new User();
            user1.setUserId(Long.valueOf(1));
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");

            User user2 = new User();
            user2.setUserId(Long.valueOf(2));
            user2.setName("Pepe");
            user2.setSurname1("Garcia");
            user2.setSurname2("Garcia");
            user2.setEmail("pepegg@gmail.com");
            user2.setPassword("password");

            userRepo.save(user1);
            userRepo.save(user2);

            UserDTO userDTO = UserDTO.fromUser(user1);
            userDTO.setNombre("JuanNuevo");
            userDTO.setApellido1("SanchezNuevo");
            userDTO.setApellido2("SanchezNuevo");
            userDTO.setEmail("juanssNUEVO@gmail.com");

            var jwt = crearUsuarioVicerrectorado();

            var peticion = putJwt("http", host, port,"/usuarios/10",userDTO,jwt);
            var respuesta = restTemplate.exchange(peticion,Void.class);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        }
    }

    @Nested
    @DisplayName("Test de Login")
    public class LoginTests{
        @Test
        @DisplayName("Iniciar sesión con credenciales correctas")
        public void loginOK() {
            // Creamos el usuario en la base de datos
            User user = new User();
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            userService.createUser(user);

            LoginDTO credentials = new LoginDTO("juanss@gmail.com", "password");

            var request = post("http", host, port, "/usuarios/login", credentials);
            var response = restTemplate.exchange(request, new ParameterizedTypeReference<RespuestaTokenDTO>() {});

            assertThat(response.getStatusCode().value()).isEqualTo(200);

            RespuestaTokenDTO respuestaToken = response.getBody();
            assertThat(
                    jwtUtil.validateToken(
                            respuestaToken.getJwt(),
                            userService.loadUserByUsername("juanss@gmail.com"))
                    ).isTrue();
        }

        @Test
        @DisplayName("Iniciar sesión con usuario no registrado")
        public void loginNotRegistered() {

            LoginDTO credentials = new LoginDTO("juanss@gmail.com", "password");
            
            var request = post("http", host, port, "/login", credentials);

            var response = restTemplate.exchange(request, new ParameterizedTypeReference<RespuestaTokenDTO>() {});

            assertThat(response.getStatusCode().value()).isEqualTo(403);
        }

        @Test
        @DisplayName("Iniciar sesión con contraseña incorrecta")
        public void loginWrongPassword() {
            // Creamos el usuario en la base de datos
            User user = new User();
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            userService.createUser(user);

            LoginDTO credentials = new LoginDTO("juanss@gmail.com", "incorrecta");

            var request = post("http", host, port, "/usuarios/login", credentials);
            var response = restTemplate.exchange(request, Void.class);

            assertThat(response.getStatusCode().value()).isEqualTo(403);
        }

    }

    @Nested
    @DisplayName("Test de Password Reset")
    public class PasswordResetTests {

        @Test
        @DisplayName("Resetear contraseña con email correcto")
        public void passwordResetOK() {
            // Creamos el usuario en la base de datos
            User user = new User();
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            userService.createUser(user);

            PasswordresetDTO passwordresetDTO = new PasswordresetDTO("juanss@gmail.com");

            var request = post("http", host, port, "/usuarios/passwordreset", passwordresetDTO);
            var response = restTemplate.exchange(request, Void.class);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(userService.correctPassword("juanss@gmail.com", "password")).isFalse();
        }

        @Test
        @DisplayName("Resetear contraseña de usuario inexistente")
        public void NonExistentUserPasswordReset() {

            PasswordresetDTO passwordresetDTO = new PasswordresetDTO("juanss@gmail.com");

            var request = post("http", host, port, "/usuarios/passwordreset", passwordresetDTO);
            var response = restTemplate.exchange(request, Void.class);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
        }

    }

    @Nested
    @DisplayName("Creación de usuarios")
    public class userCreation {

        @Test
        @DisplayName("Usuario creado correctamente (201)")
        public void correctUserCreation(){
            var userDTO = UserDTO.builder()
                    .nombre("usuario1")
                    .apellido1("apellido1")
                    .apellido2("apellido2")
                    .email("usuario@email.es")
                    .build();

            var jwt = crearUsuarioVicerrectorado();

            var peticion = postJwt("http", host, port, "/usuarios", userDTO, jwt);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            var maybeUser = userRepo.findByEmail("usuario@email.es");

            assertThat(maybeUser).isNotEmpty();

            var user = maybeUser.get();

            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:"+port+"/usuarios");

            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .endsWith("/"+user.getUserId());
        }

        /* Las autorizaciones de este recurso están comentadas para hacer pruebas con el frontend. Test inutilizado. */
        @Test
        @DisplayName("Usuario no creado por acceso no autorizado (403)")
        public void nonVicerrectorUserCreation(){
            var userDTO = UserDTO.builder()
                    .nombre("usuario1")
                    .apellido1("apellido1")
                    .apellido2("apellido2")
                    .email("usuario@email.es")
                    .build();

            var jwt = crearUsuarioCorrector();

            var peticion = postJwt("http", host, port, "/usuarios", userDTO, jwt);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }

        @Test
        @DisplayName("Usuario no creado por coincidencia de email de usuario ya registrado (409)")
        public void sameEmailUserCreation(){
            var userDTO = UserDTO.builder()
                    .nombre("usuario1")
                    .apellido1("apellido1")
                    .apellido2("apellido2")
                    .email("usuario@email.es")
                    .build();

            var user = userDTO.user();
            user.setPassword("password");
            userRepo.save(user);

            var jwt = crearUsuarioVicerrectorado();

            var peticion = postJwt("http", host, port, "/usuarios", userDTO, jwt);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }
    }

    @Nested
    @DisplayName("Eliminación de usuarios")
    public class UserDelete {
        @Test
        @DisplayName("Usuario eliminado correctamente (200)")
        public void correctDelete(){
            var userDTO = UserDTO.builder()
                    .nombre("usuario1")
                    .apellido1("apellido1")
                    .apellido2("apellido2")
                    .email("usuario@email.es")
                    .build();

            var user = userDTO.user();
            user.setPassword("1234");
            userRepo.save(user);

            var jwt = crearUsuarioVicerrectorado();

            // Hay dos usuarios el que tiene rol de vicerrectorado y el usuario
            assertThat(userRepo.findAll()).hasSize(2);

            var usuario = userRepo.findByEmail("usuario@email.es").get();

            var peticion = deleteJwt("http", host, port, "/usuarios/"+usuario.getUserId(), jwt);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

            // El usuario ha sido correctamente eliminado
            assertThat(userRepo.findAll()).hasSize(1);
            var deleteUser = userRepo.findByEmail("usuario@email.es");
            assertThat(deleteUser).isEmpty();
        }

        @Test
        @DisplayName("Usuario no eliminado por acceso no autorizado (403)")
        public void UnauthorizedDelete(){
            var userDTO = UserDTO.builder()
                    .nombre("usuario1")
                    .apellido1("apellido1")
                    .apellido2("apellido2")
                    .email("usuario@email.es")
                    .build();

            var user = userDTO.user();
            user.setPassword("1234");
            userRepo.save(user);

            var jwt = crearUsuarioCorrector();

            // Hay dos usuarios el que tiene rol de corrector y el usuario
            assertThat(userRepo.findAll()).hasSize(2);

            var usuario = userRepo.findByEmail("usuario@email.es").get();

            var peticion = deleteJwt("http", host, port, "/usuarios/"+usuario.getUserId(), jwt);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }

        @Test
        @DisplayName("Usuario inexistente (404)")
        public void NonExistentUserDelete(){

            var jwt = crearUsuarioVicerrectorado();

            var id = 100L;

            var peticion = deleteJwt("http", host, port, "/usuarios/"+
                    (userRepo.existsById(id)? id : id+1), jwt);
            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
    }

}
