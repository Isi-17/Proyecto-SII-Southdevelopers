package com.uma.southdevelopers;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.uma.southdevelopers.dto.NotificationDTO;
import com.uma.southdevelopers.dto.UserDTO;
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
    @DisplayName("Creacion de Usuarios")
    public class CreacionUsuario{
        @Test
        @DisplayName("Crear usuario")
        public void crearUsuario(){
            User user1 = new User();
            user1.setUserId(Long.valueOf(1));
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.CORRECTOR);
            user1.setRoles(roles);

            userRepo.save(user1);       //Luego ya tendremos un usuario en la bbdd y por tanto no estará vacia

            var jwt = crearUsuarioVicerrectorado();

            var userDTO = UserDTO.builder()
                    .id(Long.valueOf(66667777))
                    .nombre("Paco")
                    .apellido1("Garcia")
                    .apellido2("Garcia")
                    .email("pepegg@gmail.com")
                    .build();

            // Preparamos la petición con el usuario dentro
            var peticion = postJwt("http", host, port, "/usuarios", userDTO,jwt);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion,Void.class);

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:"+port+"/usuarios");

            List<User> usuariosBD = userRepo.findAll();
            assertThat(usuariosBD).hasSize(3);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .endsWith("/"+usuariosBD.get(2).getUserId());

            //Comprobamos los parametros
            compruebaCampos(usuariosBD.get(2),userDTO.user()); //Por que da fallo con el id ????????????
        }

        @Test
        @DisplayName("Crear usuario con un email existente")
        public void crearUsuarioExistenteEmail(){
            User user1 = new User();
            user1.setUserId(Long.valueOf(1));
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.CORRECTOR);
            user1.setRoles(roles);

            userRepo.save(user1);       //Luego ya tendremos un usuario en la bbdd y por tanto no estará vacia

            //Tratemos de crear un usuario con el mismo email
            var userDTO = UserDTO.builder()
                    .id(Long.valueOf(66667777))
                    .nombre("Paco")
                    .apellido1("Garcia")
                    .apellido2("Garcia")
                    .email("juanss@gmail.com")
                    .build();

            var jwt = crearUsuarioVicerrectorado();

            var peticion = postJwt("http", host, port, "/usuarios", userDTO,jwt);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion,Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }


    }

    @Nested
    @DisplayName("Acceso a Usuarios")
    public class AccesoUsuario{
        @Test
        @DisplayName("Acceder a usuario existente")
        public void accederUsuarioExistente(){
            User user1 = new User();
            user1.setUserId(Long.valueOf(1));
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.CORRECTOR);
            user1.setRoles(roles);

            userRepo.save(user1);       //Luego ya tendremos un usuario en la bbdd y por tanto no estará vacia

            var jwt = crearUsuarioVicerrectorado();

            var peticion = getJwt("http", host, port, "/usuarios/1",jwt);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

            UserDTO userDTORespuesta = respuesta.getBody();

            compruebaCampos(user1,userDTORespuesta.user());
        }

        @Test
        @DisplayName("Acceder a lista de usuarios")
        public void accederListaUsuarios(){
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

            var jwt = crearUsuarioVicerrectorado();

            var peticion = getJwt("http", host, port, "/usuarios",jwt);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<UserDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(3);

            compruebaCampos(user1,respuesta.getBody().get(0).user());
            compruebaCampos(user2,respuesta.getBody().get(1).user());
        }

        @Test
        @DisplayName("Acceder a usuario no existente")
        public void accederUsuarioNoExistente(){
            User user1 = new User();
            user1.setUserId(Long.valueOf(1));
            user1.setName("Juan");
            user1.setSurname1("Sanchez");
            user1.setSurname2("Sanchez");
            user1.setEmail("juanss@gmail.com");
            user1.setPassword("password");
            Set<User.Role> roles = new HashSet<>();
            roles.add(User.Role.CORRECTOR);
            user1.setRoles(roles);

            userRepo.save(user1);       //Luego ya tendremos un usuario en la bbdd y por tanto no estará vacia

            var jwt = crearUsuarioVicerrectorado();

            var peticion = getJwt("http", host, port, "/usuarios/10",jwt);

            var respuesta = restTemplate.exchange(peticion,
                    Void.class);

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
    }

    @Nested
    @DisplayName("Delete de Usuarios")
    public class DeleteUsuario{
        @Test
        @DisplayName("Delete de usuario")
        public void deleteUsuario(){
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

            var jwt = crearUsuarioVicerrectorado();

            var peticion = deleteJwt("http", host, port,"/usuarios/1",jwt);
            var respuesta = restTemplate.exchange(peticion,Void.class);
            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);

            assertThat(userRepo.existsById(Long.valueOf(1))).isFalse();
        }

        @Test
        @DisplayName("Delete de usuario que no existe")
        public void deleteUsuarioNoExiste(){
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

            var jwt = crearUsuarioVicerrectorado();

            var peticion = deleteJwt("http", host, port,"/usuarios/10",jwt);
            var respuesta = restTemplate.exchange(peticion,Void.class);
            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

            assertThat(userRepo.findAll()).hasSize(3);
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
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

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
            user.setUserId(Long.valueOf(1));
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            // ROL USUARIO
            // Set<User.Role> roles = new HashSet<>();
            // roles.add(User.Role.CORRECTOR);
            // user.setRoles(roles);

            userRepo.save(user);

            UserDTO credentials = UserDTO.builder()
                    .nombre("Juan")
                    .apellido1("password")
                    .build();
                    
            var request = post("http", host, port, "/login", credentials);
            var response = restTemplate.exchange(request, String.class);


            assertThat(response.getStatusCode()).isEqualTo(200);
            assertThat(response.getBody()).contains("accessToken");
        }

        @Test
        @DisplayName("Iniciar sesión con usuario no registrado")
        public void loginNotRegistered() {

            UserDTO credentials = UserDTO.builder()
                    .nombre("username")
                    .apellido1("password")
                    .build();
            
            var request = post("http", host, port, "/login", credentials);

            var response = restTemplate.exchange(request, String.class);

            assertThat(response.getStatusCode()).isEqualTo(403);
        }

        @Test
        @DisplayName("Iniciar sesión con contraseña incorrecta")
        public void loginWrongPassword() {

            User user = new User();
            user.setUserId(Long.valueOf(1));
            user.setName("Juan");
            user.setSurname1("Sanchez");
            user.setSurname2("Sanchez");
            user.setEmail("juanss@gmail.com");
            user.setPassword("password");
            // ROL USUARIO
            // Set<User.Role> roles = new HashSet<>();
            // roles.add(User.Role.CORRECTOR);
            // user.setRoles(roles);

            userRepo.save(user);

            UserDTO credentials = UserDTO.builder()
                    .nombre("Juan")
                    .apellido1("wrong_password")
                    .build();
            
            var request = post("http", host, port, "/login", credentials);
            var response = restTemplate.exchange(request, String.class);

            assertThat(response.getStatusCode()).isEqualTo(403);
        }

    }

    @Nested
    @DisplayName("Test de Password Reset")
    public class PasswordResetTests {
            @Test
            @DisplayName("Resetear contraseña con email correcto")
            public void passwordResetOK() throws Exception {
            
                String email = "juanss@gmail.com";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                var request = new HttpEntity<>("{\"email\":\"" + email + "\"}", headers);
                
                var response = restTemplate.postForEntity("/usuarios/passwordreset", request, String.class);
                
                assertThat(response.getStatusCode()).isEqualTo(200);
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

    /*@Nested
    @DisplayName("Con la Base de Datos Vacia")
    public class BaseDeDatosVacia {
        @Test
        @DisplayName("Acceder a un usuario concreto")
        public void errorConUsuarioConcreto() {
            var peticion = get("http", host, port, "/usuarios/1");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
        @Test
        @DisplayName("Lista vacía de usuarios")
        public void devuelveListaVaciaUsuarios() {
            var peticion = get("http", host, port, "/usuarios");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<User>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

        @Test
        @DisplayName("Crear usuario")
        public void insertaUsuario() {

            // Preparamos el usuario a insertar
            var userDTO = UserDTO.builder()
                    .id(Long.valueOf(66667777))
                    .nombre("Paco")
                    .apellido1("Garcia")
                    .apellido2("Garcia")
                    .email("pepegg@gmail.com")
                    .build();
            // Preparamos la petición con el usuario dentro
            var peticion = post("http", host, port, "/usuarios", userDTO);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion,Void.class);

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:"+port+"/usuarios");

            List<User> usuariosBD = userRepo.findAll();
            assertThat(usuariosBD).hasSize(1);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .endsWith("/"+usuariosBD.get(0).getUserId());

            //Comprobamos los parametros
            compruebaCampos(usuariosBD.get(0),userDTO.user()); //Por que da fallo con el id ????????????
        }

        @Test
        @DisplayName("Delete de usuario")
        public void deleteUsuarioNoExiste(){
            var peticion = delete("http", host, port,"/usuarios/3");
            var respuesta = restTemplate.exchange(peticion,Void.class);
            assertThat(respuesta.getStatusCode()).isEqualTo(404);
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

            UserDTO userDTO = UserDTO.fromUser(user1);
            userDTO.setNombre("JuanNuevo");
            userDTO.setApellido1("SanchezNuevo");
            userDTO.setApellido2("SanchezNuevo");
            userDTO.setEmail("juanssNUEVO@gmail.com");

            var peticion = put("http", host, port,"/usuarios/3",userDTO);
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode()).isEqualTo(404);

        }

    }*/
}