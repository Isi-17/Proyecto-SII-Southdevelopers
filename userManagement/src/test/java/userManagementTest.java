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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
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

    private void compruebaCampos(User user, UserDTO userDTO){
        assertThat(user.getUserId()).isEqualTo(userDTO.getId());
        assertThat(user.getName()).isEqualTo(userDTO.getNombre());
        assertThat(user.getSurname1()).isEqualTo(userDTO.getApellido1());
        assertThat(user.getSurname2()).isEqualTo(userDTO.getApellido2());
        assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
    }

    @Nested
    @DisplayName("Con la Base de Datos Vacia")
    public class BaseDeDatosVacia {

        @Test
        @DisplayName("Acceder a un usuario concreto")
        public void errorConUsuarioConcreto() {
            var peticion = get("http", host, port, "/usuarios/1");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<User>() {});

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
        @DisplayName("Inserta un usuario")
        public void insertaUsuario() {

            // Preparamos el usuario a insertar
            var userDTO = UserDTO.builder()
                    .id(66667777)
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
            compruebaCampos(usuariosBD.get(0),userDTO);
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
                    .id(66667777)
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
            compruebaCampos(usuariosBD.get(1),userDTO);
        }
    }
}
