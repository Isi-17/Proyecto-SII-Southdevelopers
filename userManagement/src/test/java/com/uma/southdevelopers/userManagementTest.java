package com.uma.southdevelopers;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.uma.southdevelopers.dto.NotificationDTO;
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

            var peticion = get("http", host, port, "/usuarios/2");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:"+port+"/usuarios");
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

            var peticion = delete("http", host, port,"/usuarios/3");
            var respuesta = restTemplate.exchange(peticion,Void.class);
            assertThat(respuesta.getStatusCode()).isEqualTo(404);

            assertThat(userRepo.findAll()).hasSize(2);
        }

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

            var peticion = put("http", host, port,"/usuarios/1",userDTO);
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode()).isEqualTo(200);

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

            var peticion = put("http", host, port,"/usuarios/3",userDTO);
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<UserDTO>() {});

            assertThat(respuesta.getStatusCode()).isEqualTo(404);

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

    @Nested
    @DisplayName("Test de Password Reset")
    public class PasswordResetTests {
        //     @Test
        //     @DisplayName("Resetear contraseña con email correcto")
        //     public void passwordResetOK() throws Exception {
            
        //         String email = "juanss@gmail.com";
        //         HttpHeaders headers = new HttpHeaders();
        //         headers.setContentType(MediaType.APPLICATION_JSON);
        //         var request = new HttpEntity<>("{\"email\":\"" + email + "\"}", headers);
                
        //         var response = restTemplate.postForEntity("/usuarios/passwordreset", request, String.class);
                
        //         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //     }
        // }
    }
    @Nested
    @DisplayName("Test Acceso&Token")
    public class AccesoTokenTests{

        @Test
        @DisplayName("Acceso a /usuarios sin token")
        public void accesoUsuariosSinToken() {
            var request = get("http", host, port, "/usuarios");
            var response = restTemplate.exchange(request, new ParameterizedTypeReference<List<UserDTO>>() {});
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("Acceso a /usuarios con token")
        public void accesoUsuariosConToken(){

        }

        // @Test
        // @DisplayName("Acceso a /usuarios/{id} sin token")
        // public void accesoUsuarioSinTokenID() {
        //     var request = get("http", host, port, "/usuarios/1");
        //     var response = restTemplate.exchange(request, new ParameterizedTypeReference<UserDTO>() {});
        //     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        // }

        // @Test
        // @DisplayName("Acceso a /usuarios/{id} con token")
        // public void accesoUsuarioConTokenID(){

        // }

        @Test
        @DisplayName("Acceso a /usuarios/{id} con token de otro usuario")
        public void accesoUsuarioConTokenDeOtroUsuario(){
        
        }

        // @Test
        // @DisplayName("Acceso a /usuarios/{id} con token de otro usuario")
        // public void accesoUsuarioConTokenDeOtroUsuarioID(){
        
        // }

        @Test
        @DisplayName("Acceso a un recurso sin autenticación")
        public void accesoRecursoSinAutenticacion(){
           
        }

        // Este test no funciona porque no se puede inyectar el MockMvc
        @Autowired
        private MockMvc mockMvc;

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        @DisplayName("Acceso del admin a un recurso protegido")
        public void testAdminAccessToAdminResource() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/admin-resource")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "user", roles = "USER")
        @DisplayName("Acceso de usuario no autorizado a un recurso protegido")
        public void testUserAccessToAdminResource() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/admin-resource")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Acceso a un recurso no protegido")
        public void testAccessToUnsecuredResource() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/unsecured-resource")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Acceso a un recurso protegido sin autenticación")
        public void testAccessToSecuredResourceWithoutAuthentication() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/secured-resource")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        // @Test
        // public void testAccessToSecuredResourceWithAuthentication() throws Exception {
        //     String token = getToken("user", "password");

        //     mockMvc.perform(get("/secured-resource")
        //             .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        //             .andExpect(status().isOk());
        // }

        // private String getToken(String username, String password) throws Exception {
        //     AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        //     authenticationRequest.setUsername(username);
        //     authenticationRequest.setPassword(password);

        //     MvcResult result = mockMvc.perform(post("/authenticate")
        //             .contentType(MediaType.APPLICATION_JSON)
        //             .content(objectMapper.writeValueAsString(authenticationRequest)))
        //             .andReturn();

        //     String tokenJson = result.getResponse().getContentAsString();
        //     Map<String, String> map = objectMapper.readValue(tokenJson, new TypeReference<Map<String, String>>() {});

        //     return map.get("token");
        // }
    }

    @Nested
    @DisplayName("Test de Notificaciones")
    public class NotificacionesTests {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Envio de notificacion exitoso")
        public void enviarNotificacionExitoso() throws Exception {
            NotificationDTO notificacion = NotificationDTO.builder()
                    .asunto("Prueba de notificación")
                    .cuerpo("Esta es una prueba de notificación")
                    .emailDestino("destinatario@ejemplo.com")
                    .telefonoDestino("+34666666666")
                    .programacionEnvio("2023-04-25T10:30:00Z")
                    .medios(Arrays.asList("email", "sms"))
                    .tipoDeNotificacion("importante")
                    .build();
    
             // Me daba fallos el objeto objectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(notificacion);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/notification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notificacion))
            );
    
            result.andExpect(status().isOk());
        }
    
        @Test
        @DisplayName("Envío de notificación sin datos requeridos")
        public void enviarNotificacionSinDatos() throws Exception {
            NotificationDTO notificacion = NotificationDTO.builder().build();
    
            // Me daba fallos el objeto objectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(notificacion);

            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/notification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(notificacion))
            );
    
            result.andExpect(status().isBadRequest());
        }

    }

}
