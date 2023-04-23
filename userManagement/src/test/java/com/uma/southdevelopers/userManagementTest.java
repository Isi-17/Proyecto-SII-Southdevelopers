package com.uma.southdevelopers;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.uma.southdevelopers.dto.UserDTO;
import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.repositories.UserRepository;
import lombok.NoArgsConstructor;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DisplayName("Tests de userManagement")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class userManagementTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    @Value(value="${local.server.host}")
    private String host;

    @Autowired
    private UserRepository userRepo;

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

    @Nested
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

    }

    @Nested
    @DisplayName("Con Datos en la Base de Datos")
    public class BaseDeDatosNoVacia{
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
            assertThat(usuariosBD).hasSize(2);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .endsWith("/"+usuariosBD.get(1).getUserId());

            //Comprobamos los parametros
            compruebaCampos(usuariosBD.get(1),userDTO.user()); //Por que da fallo con el id ????????????
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

            var peticion = post("http", host, port, "/usuarios", userDTO);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion,Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
        }

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

            var peticion = get("http", host, port, "/usuarios/1");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:"+port+"/usuarios");

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


            var peticion = get("http", host, port, "/usuarios");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<UserDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(2);

            compruebaCampos(user1,respuesta.getBody().get(0).user());
            compruebaCampos(user2,respuesta.getBody().get(1).user());
        }

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

            var peticion = delete("http", host, port,"/usuarios/1");
            var respuesta = restTemplate.exchange(peticion,Void.class);
            assertThat(respuesta.getStatusCode()).isEqualTo(200);

            assertThat(userRepo.existsById(Long.valueOf(1))).isFalse();
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


            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
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

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

    }
}
