package com.uma.southdevelopers;

import com.uma.southdevelopers.dtos.InstituteDTO;
import com.uma.southdevelopers.dtos.NewStudentDTO;
import com.uma.southdevelopers.dtos.CompleteNameDTO;
import com.uma.southdevelopers.dtos.StudentDTO;
import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.springframework.core.ParameterizedTypeReference;
import org.junit.jupiter.api.Test;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.repositories.InstituteRepository;
import com.uma.southdevelopers.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private SubjectRepository materiaRepo;

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
        assertThat(actual.getApellido1()).isEqualTo(expected.getApellido1());
        assertThat(actual.getApellido2()).isEqualTo(expected.getApellido2());
        assertThat(actual.getDni()).isEqualTo(expected.getDni());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getIdSede()).isEqualTo(expected.getIdSede());
        assertThat(actual.getTelefono()).isEqualTo(expected.getTelefono());
        assertThat(actual.getInstituto()).isEqualTo(expected.getInstituto());

        List<Enrolment> actualMatriculas = actual.getMatriculas();
        List<Enrolment> expectedMatriculas = expected.getMatriculas();
        assertThat(actualMatriculas.size()).isEqualTo(expectedMatriculas.size());
        for(int i = 0; i < actualMatriculas.size(); i++) {
            assertThat(actualMatriculas.get(i).getId()).isEqualTo(expectedMatriculas.get(i).getId());
            for(int j = 0; j < actualMatriculas.get(i).getMateriasMatriculadas().size(); j++) {
                assertThat(actualMatriculas.get(i).getMateriasMatriculadas().get(j).getId())
                        .isEqualTo(expectedMatriculas.get(i).getMateriasMatriculadas().get(j).getId());
                assertThat(actualMatriculas.get(i).getMateriasMatriculadas().get(j).getNombre())
                        .isEqualTo(expectedMatriculas.get(i).getMateriasMatriculadas().get(j).getNombre());

            }
        }
    }

    private void checkFields(StudentDTO expected, Student actual) {
        assertThat(actual.getNombre()).isEqualTo(expected.getNombreCompleto().getNombre());
        assertThat(actual.getApellido1()).isEqualTo(expected.getNombreCompleto().getApellido1());
        assertThat(actual.getApellido2()).isEqualTo(expected.getNombreCompleto().getApellido2());
        assertThat(actual.getDni()).isEqualTo(expected.getDni());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getIdSede()).isEqualTo(expected.getIdSede());
        assertThat(actual.getTelefono()).isEqualTo(expected.getTelefono());
        assertThat(actual.getInstituto()).isEqualTo(expected.getInstituto());
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
    @DisplayName("Cuando la base de datos está vacía")
    public class EmptyDataBase {

        @Nested
        @DisplayName("Pruebas para los metodos get")
        public class get {
            @Test
            @DisplayName("Devuelve una lista vacía de estudiantes y devuelve 200")
            public void devuelveListaVaciaEstudiantes(){
                var peticion = get("http", "localhost", port, "/estudiantes");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                assertThat(respuesta.getBody()).isEmpty();
            }

            @Test
            @DisplayName("Devuelve una lista vacía de estudiantes con idSede y devuelve 200")
            public void devuelveListaVaciaEstudiantesConIdSede(){

                String baseUrl = "http://localhost:"+port+"/estudiantes";

                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 0);

                String url = builder.toUriString();

                ResponseEntity<List<StudentDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(response.getStatusCode().value()).isEqualTo(200);
                assertThat(response.getBody()).isEmpty();
            }

            @Test
            @DisplayName("Devuelve una lista vacía de estudiantes con idConvocatoria y devuelve 200")
            public void devuelveListaVaciaEstudiantesConIdConvocatoria(){

                String baseUrl = "http://localhost:"+port+"/estudiantes";

                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idConvocatoria", 2023);

                String url = builder.toUriString();

                ResponseEntity<List<StudentDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(response.getStatusCode().value()).isEqualTo(200);
                assertThat(response.getBody()).isEmpty();
            }

            @Test
            @DisplayName("Devuelve una lista vacía de institutos y devuelve 200")
            public void devuelveListaVaciaInstitutos(){
                var peticion = get("http", "localhost", port, "/institutos");
                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<InstituteDTO>>() {});
                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
                assertThat(respuesta.getBody()).isEmpty();
            }

        }


        @Nested
        @DisplayName("Pruebas para los metodos get con un id")
        public class getId {
            @Test
            @DisplayName("Devuelve error 404 al acceder a un estudiante concreto")
            public void errorConEstudianteConcreto() {
                var peticion = get("http", "localhost", port, "/estudiantes/1");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            }

            @Test
            @DisplayName("Devuelve error 404 al acceder a un instituto concreto")
            public void errorConInstitutoConcreto() {
                var peticion = get("http", "localhost", port, "/institutos/1");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<InstituteDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
            }
        }


        @Nested
        @DisplayName("Pruebas para los metodos post")
        public class post {
            @Test
            @DisplayName("Inserta correctamente un estudiante y devuelve 201")
            public void insertaEstudiante(){

                // Subimos primero un instituto
                Institute instituto = new Institute();
                instituto.setNombre("IES");
                instituto.setId(1L);

                restTemplate.exchange(post("http", "localhost", port, "/institutos",
                        instituto), Void.class);

                // Subimos una materia
                Subject materia = new Subject();
                materia.setNombre("Matematicas");
                materia.setId(1L);

                restTemplate.exchange(post("http", "localhost", port, "/materias",
                        materia), Void.class);

                // Vamos a subir el estudiante
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Jesus");
                cn.setApellido1("Escudero");
                cn.setApellido2("Moreno");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(1L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);

                System.out.println(estudiantesBD.get(0).getNombre());

                List<Subject> materias = new ArrayList<>();
                materias.add(materia);
                List<Enrolment> matriculas = new ArrayList<>();
                matriculas.add(new Enrolment(1L, 2023L, materias));
                checkFields(estudiante.student(instituto, matriculas), estudiantesBD.get(0));
            }

            @Test
            @DisplayName("Inserta correctamente un instituto y devuelve 201")
            public void insertaInstituto(){

                var instituto = InstituteDTO.builder()
                        .nombre("Puertosol")
                        .build();

                var peticion = post("http", "localhost", port, "/institutos", instituto);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

                List<Institute> institutosBD = instituteRepo.findAll();
                assertThat(institutosBD).hasSize(1);
                checkFields(instituto.institute(), institutosBD.get(0));
            }
        }


        @Nested
        @DisplayName("Pruebas para los metodos delete")
        public class delete {
            @Test
            @DisplayName("Devuelve error 404 al borrar un estudiante")
            public void eliminaEstudiante(){

                var peticionDelete = delete("http", "localhost", port, "/estudiantes/1");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(404);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(0);
            }

            @Test
            @DisplayName("Devuelve error 404 al borrar un instituto")
            public void eliminaInstituto(){

                var peticionDelete = delete("http", "localhost", port, "/institutos/1");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(404);

                List<Institute> institutosBD = instituteRepo.findAll();

                assertThat(institutosBD).hasSize(0);
            }
        }

        @Nested
        @DisplayName("Pruebas para los metodos put")
        public class put {
            @Test
            @DisplayName("Devuelve error 404 al modificar un estudiante")
            public void actualizaEstudiante(){

                NewStudentDTO newStudentDTO = new NewStudentDTO();
                CompleteNameDTO nombreCompleto = new CompleteNameDTO("Alvaro", "Sanchez", "Hernandez");
                newStudentDTO.setNombreCompleto(nombreCompleto);
                newStudentDTO.setDni("12345678A");
                newStudentDTO.setMateriasMatriculadas(List.of());
                newStudentDTO.setIdInstituto(1L);
                newStudentDTO.setIdSede(1L);
                newStudentDTO.setNoEliminar(false);

                var peticionPut = put("http", "localhost", port, "/estudiantes/1", newStudentDTO);

                var respuestaPut = restTemplate.exchange(peticionPut, Void.class);
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(404);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(0);
            }

            @Test
            @DisplayName("Devuelve error 404 al modificar un instituto")
            public void actualizaInstituto(){

                Institute instituto = new Institute();

                var peticionPut = put("http", "localhost", port, "/institutos/1", instituto);

                var respuestaPut = restTemplate.exchange(peticionPut, Void.class);
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(404);

                List<Institute> institutosBD = instituteRepo.findAll();

                assertThat(institutosBD).hasSize(0);
            }
        }

    }

    @Nested
    @DisplayName("Cuando la base de datos tiene elementos")
    public class ElementsInDataBase{
        @BeforeEach
        public void setUp(){

            // Subimos primero un instituto
            Institute instituto = new Institute();
            instituto.setNombre("IES");
            instituto.setId(1L);

            restTemplate.exchange(post("http", "localhost", port, "/institutos",
                    instituto), Void.class);

            // Subimos una materia
            Subject materia = new Subject();
            materia.setNombre("Matematicas");
            materia.setId(1L);

            restTemplate.exchange(post("http", "localhost", port, "/materias",
                    materia), Void.class);

            // Vamos a subir el estudiante
            CompleteNameDTO cn = new CompleteNameDTO();
            cn.setNombre("Alvaro");
            cn.setApellido1("Sanchez");
            cn.setApellido2("Hernandez");

            var estudiante = NewStudentDTO.builder()
                    .id(1L)
                    .materiasMatriculadas(List.of(materia.getId()))
                    .idInstituto(instituto.getId())
                    .nombreCompleto(cn)
                    .idSede(0L)
                    .dni("12345678A")
                    .build();

            var peticion = post("http", "localhost", port, "/estudiantes",
                    estudiante);

            restTemplate.exchange(peticion, Void.class);

        }

        @Nested
        @DisplayName("Pruebas para los metodos get")
        public class get {

            @Test
            @DisplayName("Devuelve la lista de estudiantes(Sin idSede ni idConvocatoria) y devuelve 200")
            public void devuelveListaEstudiantes() {

                var peticion = get("http", "localhost", port, "/estudiantes");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                List<StudentDTO> estudiantesBD = respuesta.getBody();

                assertThat(estudiantesBD).hasSize(1);
            }

            @Test
            @DisplayName("Devuelve la lista de estudiantes(Con idSede y sin idConvocatoria) y devuelve 200")
            public void devuelveListaEstudiantesConIdSede(){
                // Comprobamos que si pedimos idSede=0 nos devuelve el estudiante
                String baseUrl = "http://localhost:"+port+"/estudiantes";

                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 0);

                String url = builder.toUriString();

                ResponseEntity<List<StudentDTO>> respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                List<StudentDTO> estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(1);


                // Comprobamos que si pedimos idSede=1 nos devuelve una lista vacia

                builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 1);

                url = builder.toUriString();

                respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(0);
            }

            @Test
            @DisplayName("Devuelve la lista de estudiantes(Sin idSede y con idConvocatoria) y devuelve 200")
            public void devuelveListaEstudiantesConIdConvocatoria(){
                // Comprobamos que si pedimos idConvocatoria=2023 nos devuelve el estudiante
                String baseUrl = "http://localhost:"+port+"/estudiantes";

                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idConvocatoria", 2023);

                String url = builder.toUriString();

                ResponseEntity<List<StudentDTO>> respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                List<StudentDTO> estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(1);


                // Comprobamos que si pedimos idConvocatoria=2021 nos devuelve una lista vacia

                builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idConvocatoria", 2021);

                url = builder.toUriString();

                respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(0);
            }

            @Test
            @DisplayName("Devuelve la lista de estudiantes(Con idSede y con idConvocatoria) y devuelve 200")
            public void devuelveListaEstudiantesConIdSedeConIdConvocatoria(){
                // Comprobamos que si pedimos idSede=0 y idConvocatoria=2023 nos devuelve el estudiante
                String baseUrl = "http://localhost:"+port+"/estudiantes";

                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 0)
                        .queryParam("idConvocatoria", 2023);

                String url = builder.toUriString();

                ResponseEntity<List<StudentDTO>> respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                List<StudentDTO> estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(1);


                // Comprobamos que si pedimos idSede=0 y idConvocatoria=2021 nos devuelve una lista vacia

                builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 0)
                        .queryParam("idConvocatoria", 2021);

                url = builder.toUriString();

                respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(0);

                // Comprobamos que si pedimos idSede=1 y idConvocatoria=2023 nos devuelve una lista vacia

                builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 1)
                        .queryParam("idConvocatoria", 2023);

                url = builder.toUriString();

                respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(0);

                // Comprobamos que si pedimos idSede=1 y idConvocatoria=2021 nos devuelve una lista vacia

                builder = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("idSede", 1)
                        .queryParam("idConvocatoria", 2021);

                url = builder.toUriString();

                respuesta = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<StudentDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                estudiantesBD = respuesta.getBody();
                assertThat(estudiantesBD).hasSize(0);
            }

            @Test
            @DisplayName("Devuelve la lista de institutos y devuelve 200")
            public void devuelveListaInstitutos() {

                var peticion = get("http", "localhost", port, "/institutos");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<List<InstituteDTO>>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                List<InstituteDTO> institutosBD = respuesta.getBody();

                assertThat(institutosBD).hasSize(1);
            }

        }

        @Nested
        @DisplayName("Pruebas para los metodos get con id")
        public class getId {

            @Test
            @DisplayName("Devuelve el estudiante y devuelve 200")
            public void devuelveEstudianteConId() {
                var peticion = get("http", "localhost", port, "/estudiantes/1");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<StudentDTO>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

                Optional<Student> expectedStudent = studentRepo.findById(1L);
                expectedStudent.ifPresent(student -> checkFields(respuesta.getBody(), student));
            }

            @Test
            @DisplayName("Devuelve 404 si no encuentra el estudiante")
            public void devuelveEstudianteConIdNoExistente() {
                var peticion = get("http", "localhost", port, "/estudiantes/2");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<StudentDTO>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

            }

            @Test
            @DisplayName("Devuelve el instituto y devuelve 200")
            public void devuelveInstitutoConId() {
                var peticion = get("http", "localhost", port, "/institutos/1");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<InstituteDTO>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            }

            @Test
            @DisplayName("Devuelve 404 si no encuentra el instituto")
            public void devuelveInstitutoConIdNoExistente() {
                var peticion = get("http", "localhost", port, "/institutos/2");

                var respuesta = restTemplate.exchange(peticion,
                        new ParameterizedTypeReference<InstituteDTO>() {});

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

            }

        }

        @Nested
        @DisplayName("Pruebas para los metodos post")
        public class post {
            @Test
            @DisplayName("Inserta correctamente un estudiante y devuelve 201")
            public void insertaEstudiante(){

                // Vamos a subir el estudiante
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Jesus");
                cn.setApellido1("Escudero");
                cn.setApellido2("Moreno");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(1L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(2);
            }

            @Test
            @DisplayName("Inserta un estudiante con un DNI ya existente y devuelve 409")
            public void insertaEstudianteConDniRepetido(){

                // Vamos a subir el estudiante
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Jesus");
                cn.setApellido1("Escudero");
                cn.setApellido2("Moreno");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(1L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .dni("12345678A")
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(409);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);
            }

            @Test
            @DisplayName("Inserta correctamente un instituto y devuelve 201")
            public void insertaInstituto(){

                var instituto = InstituteDTO.builder()
                        .nombre("Puertosol")
                        .build();

                var peticion = post("http", "localhost", port, "/institutos", instituto);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

                List<Institute> institutosBD = instituteRepo.findAll();
                assertThat(institutosBD).hasSize(2);
            }
        }

    }
}