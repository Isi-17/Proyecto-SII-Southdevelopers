import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class userManagementTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

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
    @Nested
    @DisplayName("Con la Base de Datos Vacia")
    public class BaseDeDatosVacia {
        @Test
        @DisplayName("devuelve una lista vacía de usuarios")
        public void devuelveListaVaciaProductos() {
            var peticion = get("http", "localhost",port, "");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<User>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }
    }
}
