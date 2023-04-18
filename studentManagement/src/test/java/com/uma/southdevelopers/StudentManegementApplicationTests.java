package com.uma.southdevelopers;

import com.uma.southdevelopers.dtos.InstituteDTO;
import com.uma.southdevelopers.dtos.StudentDTO;
import com.uma.southdevelopers.dtos.CompleteNameDTO;
import org.junit.jupiter.api.Nested;
import org.springframework.core.ParameterizedTypeReference;
import org.testng.annotations.Test;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.repositories.InstituteRepository;
import com.uma.southdevelopers.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de estudiantes")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentManegementApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private InstituteRepository instituteRepo;

    private URI uri(String scheme, String host, int port, String ...paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for(String path: paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        return peticion;
    }

    private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.delete(uri)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private void checkFields(Student expected, Student actual) {
        assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
        assertThat(actual.getDni()).isEqualTo(expected.getDni());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getIdSede()).isEqualTo(expected.getIdSede());
        assertThat(actual.getTelefono()).isEqualTo(expected.getTelefono());
        assertThat(actual.getInstituto()).isEqualTo(expected.getInstituto());
        assertThat(actual.getMateriasMatriculadas()).isEqualTo(expected.getMateriasMatriculadas());
    }

    private void checkFields(Institute expected, Institute actual) {
        assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
        assertThat(actual.getPais()).isEqualTo(expected.getPais());
        assertThat(actual.getLocalidad()).isEqualTo(expected.getLocalidad());
        assertThat(actual.getCodigoPostal()).isEqualTo(expected.getCodigoPostal());
        assertThat(actual.getDireccion1()).isEqualTo(expected.getDireccion1());
        assertThat(actual.getDireccion2()).isEqualTo(expected.getDireccion2());
    }

    @Nested
    @DisplayName("cuando la base de datos está vacía")
    public class EmptyDataBase {

        @Test
        @DisplayName("devuelve error al acceder a un estudiante concreto")
        public void errorConEstudianteConcreto() {
            var peticion = get("http", "localhost", port, "/localhost/1");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<StudentDTO>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve error al acceder a un instituto concreto")
        public void errorConInstitutoConcreto() {
            var peticion = get("http", "localhost", port, "/localhost/1");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<InstituteDTO>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve una lista vacía de estudiantes")
        public void devuelveListaVaciaEstudiantes(){
            var peticion = get("http", "localhost", port, "/localhost");
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<StudentDTO>>() {});
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

        @Test
        @DisplayName("devuelve una lista vacía de institutos")
        public void devuelveListaVaciaInstitutos(){
            var peticion = get("http", "localhost", port, "/localhost");
            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<InstituteDTO>>() {});
            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

        @Test
        @DisplayName("inserta correctamente un estudiante")
        public void insertaEstudiante(){

            CompleteNameDTO cn = new CompleteNameDTO();
            cn.setNombre("Jesus");
            cn.setApellido1("Escudero");
            cn.setApellido2("Moreno");

            // Preparamos el ingrediente a insertar
            var estudiante = StudentDTO.builder()
                    .nombre(cn)
                    .build();

            // Preparamos la peticion con el estudiante dentro
            var peticion = post("http", "localhost", port, "/localhost", estudiante);

            // Invocamos al servicio REST
            var respuesta = restTemplate.exchange(peticion, Void.class);

            // Comprobamos el resultado
            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(respuesta.getHeaders().get("Location").get(0))
                    .startsWith("http://localhost:" + port + "/Localhost");

            List<Student> estudiantesBD = studentRepo.findAll();
            assertThat(estudiantesBD).hasSize(1);



        }






    }
}