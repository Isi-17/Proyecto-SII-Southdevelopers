package com.uma.southdevelopers;

import com.uma.southdevelopers.dtos.*;
import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Subject;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

                List<Subject> materias = new ArrayList<>();
                materias.add(materia);
                List<Enrolment> matriculas = new ArrayList<>();
                matriculas.add(new Enrolment(1L, 2023L, materias));
                checkFields(estudiante.student(instituto, matriculas), estudiantesBD.get(0));
            }

            @Test
            @DisplayName("Inserta un estudiante, no encuentra el instituto y devuelve 404")
            public void insertaEstudianteSinInstituto(){

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

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(0);

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

        @Test
        @DisplayName("Subir csv de estudiantes")
        public void subirCSV() {
            // Datos de ejemplo
            String csvData = "CENTRO;Nombre;Apellido1;Apellido2;DNI/NIF;DETALLE_MATERIAS\n" +
                    "C.C. JUAN XXIII;Paula;Gámez;Baños;88126719U;Historia de España, Lengua Castellana y Literatura";

            String baseUrl = "http://localhost:"+port+"/estudiantes/upload";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("ficheroEstudiantes", new ByteArrayResource(csvData.getBytes()) {
                @Override
                public String getFilename() {
                    return "estudiantes.csv"; // Nombre del archivo
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Establecer el tipo de contenido a multipart/form-data

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImportacionEstudiantesDTO> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ImportacionEstudiantesDTO>() {});

            // Devuelve 200 pero no ha añadido al estudiante
            // Porque no ha encontrado el instituto ni las materias
            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(!(response.getBody().getNoImportados().isEmpty()));
            assertThat(studentRepo.findAll()).hasSize(0);
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
            Subject materia = new Subject(1L, "Matematicas", true);

            restTemplate.exchange(post("http", "localhost", port, "/materias",
                    materia), Void.class);

            // Subimos una materia
            materia = new Subject();
            materia.setNombre("Lengua");
            materia.setId(2L);

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
            @DisplayName("Inserta un estudiante con materia no existente y devuelve 404")
            public void insertaEstudianteConMateriaExistente(){

                // Vamos a subir el estudiante
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Jesus");
                cn.setApellido1("Escudero");
                cn.setApellido2("Moreno");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(3L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                var respuesta = restTemplate.exchange(peticion, Void.class);

                assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);
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

        @Nested
        @DisplayName("Pruebas para los metodos delete")
        public class delete {

            @Test
            @DisplayName("Devuelve error 404 al borrar un estudiante que no existe")
            public void eliminaEstudianteNoEncontrado(){

                var peticionDelete = delete("http", "localhost", port, "/estudiantes/10");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(404);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);
            }

            @Test
            @DisplayName("Borra un estudiante y devuelve 200")
            public void eliminaEstudiante(){

                var peticionDelete = delete("http", "localhost", port, "/estudiantes/1");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(200);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(0);
            }

            @Test
            @DisplayName("Devuelve error 409 al borrar un estudiante que no se puede eliminar")
            public void eliminaEstudianteQueNoSePuedeEliminar(){

                // Primero tenemos que añadir un estudiante que no se pueda eliminar
                // Vamos a subir el estudiante
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Alvaro");
                cn.setApellido1("Sanchez");
                cn.setApellido2("Hernandez");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(1L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .idSede(0L)
                        .dni("12345671A")
                        .noEliminar(true)
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                restTemplate.exchange(peticion, Void.class);

                var peticionDelete = delete("http", "localhost", port, "/estudiantes/2");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(409);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(2);
            }

            @Test
            @DisplayName("Devuelve error 409 al borrar un instituto con un estudiante asociado a el")
            public void eliminaInstitutoConEstudianteAsociado(){

                var peticionDelete = delete("http", "localhost", port, "/institutos/1");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(409);

                List<Institute> institutosBD = instituteRepo.findAll();

                assertThat(institutosBD).hasSize(1);
            }

            @Test
            @DisplayName("Devuelve error 404 al borrar un instituto que no existe")
            public void eliminaInstitutoNoEncontrado(){

                var peticionDelete = delete("http", "localhost", port, "/institutos/10");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(404);

                List<Institute> institutosBD = instituteRepo.findAll();

                assertThat(institutosBD).hasSize(1);
            }

            @Test
            @DisplayName("Borrar un instituto y devuelve 200")
            public void eliminaInstituto(){

                // Primero tenemos que borrar el estudiante asociado al instituto
                var peticionDeleteEstudiante = delete("http", "localhost", port, "/estudiantes/1");

                restTemplate.exchange(peticionDeleteEstudiante, Void.class);

                // Ahora si podemos borrar el instituto
                var peticionDelete = delete("http", "localhost", port, "/institutos/1");

                var respuestaDelete = restTemplate.exchange(peticionDelete, Void.class);
                assertThat(respuestaDelete.getStatusCode().value()).isEqualTo(200);

                List<Institute> institutosBD = instituteRepo.findAll();

                assertThat(institutosBD).hasSize(0);
            }
        }

        @Nested
        @DisplayName("Pruebas para los metodos put")
        public class put {

            @Test
            @DisplayName("Modificar un estudiante y devuelve 200")
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

                var respuestaPut = restTemplate.exchange(peticionPut, new ParameterizedTypeReference<StudentDTO>() {
                });
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(200);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);
                checkFields(respuestaPut.getBody(), estudiantesBD.get(0));
            }

            @Test
            @DisplayName("Modificar un estudiante (cambiando la lista de materias) y devuelve 200")
            public void actualizaEstudianteConMaterias(){

                NewStudentDTO newStudentDTO = new NewStudentDTO();
                CompleteNameDTO nombreCompleto = new CompleteNameDTO("Alvaro", "Sanchez", "Hernandez");
                newStudentDTO.setNombreCompleto(nombreCompleto);
                newStudentDTO.setDni("12345678A");
                newStudentDTO.setMateriasMatriculadas(List.of(1L, 2L));
                newStudentDTO.setIdInstituto(1L);
                newStudentDTO.setIdSede(1L);
                newStudentDTO.setNoEliminar(false);

                var peticionPut = put("http", "localhost", port, "/estudiantes/1", newStudentDTO);

                var respuestaPut = restTemplate.exchange(peticionPut, new ParameterizedTypeReference<StudentDTO>() {
                });
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(200);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);
                checkFields(respuestaPut.getBody(), estudiantesBD.get(0));
            }

            @Test
            @DisplayName("Modificar un estudiante (con materias no existentes) y devuelve 404")
            public void actualizaEstudianteConMateriasNoExistentes(){

                NewStudentDTO newStudentDTO = new NewStudentDTO();
                CompleteNameDTO nombreCompleto = new CompleteNameDTO("Alvaro", "Sanchez", "Hernandez");
                newStudentDTO.setNombreCompleto(nombreCompleto);
                newStudentDTO.setDni("12345678A");
                newStudentDTO.setMateriasMatriculadas(List.of(1L, 3L)); // La materia 3 no existe
                newStudentDTO.setIdInstituto(1L);
                newStudentDTO.setIdSede(1L);
                newStudentDTO.setNoEliminar(false);

                var peticionPut = put("http", "localhost", port, "/estudiantes/1", newStudentDTO);

                var respuestaPut = restTemplate.exchange(peticionPut, new ParameterizedTypeReference<StudentDTO>() {
                });
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(404);
            }

            @Test
            @DisplayName("Modificar un estudiante con un DNI ya existente y devuelve 409")
            public void actualizaEstudianteConDniRepetido(){

                // Vamos a subir un nuevo estudiante con otro DNI y vamos a modificarlo para que sea el mismo que el del estudiante 1
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Alvaro");
                cn.setApellido1("Sanchez");
                cn.setApellido2("Hernandez");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(1L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .idSede(0L)
                        .dni("12345671A")
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                restTemplate.exchange(peticion, Void.class);

                NewStudentDTO newStudentDTO = new NewStudentDTO();
                CompleteNameDTO nombreCompleto = new CompleteNameDTO("Alvaro", "Sanchez", "Hernandez");
                newStudentDTO.setNombreCompleto(nombreCompleto);
                newStudentDTO.setDni("12345678A");
                newStudentDTO.setMateriasMatriculadas(List.of());
                newStudentDTO.setIdInstituto(1L);
                newStudentDTO.setIdSede(1L);
                newStudentDTO.setNoEliminar(false);

                var peticionPut = put("http", "localhost", port, "/estudiantes/2", newStudentDTO);

                var respuestaPut = restTemplate.exchange(peticionPut, new ParameterizedTypeReference<StudentDTO>() {
                });
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(409);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(2);
            }

            @Test
            @DisplayName("Modificar el atributo noEliminar de True a False y comprueba que sigue estando a true")
            public void actualizaEstudianteNoEliminarTrue(){

                // Vamos a subir un nuevo estudiante con noEliminar a true
                CompleteNameDTO cn = new CompleteNameDTO();
                cn.setNombre("Alvaro");
                cn.setApellido1("Sanchez");
                cn.setApellido2("Hernandez");

                var estudiante = NewStudentDTO.builder()
                        .id(1L)
                        .materiasMatriculadas(List.of(1L))
                        .idInstituto(1L)
                        .nombreCompleto(cn)
                        .idSede(0L)
                        .dni("12345671A")
                        .noEliminar(true)
                        .build();

                var peticion = post("http", "localhost", port, "/estudiantes",
                        estudiante);

                restTemplate.exchange(peticion, Void.class);

                NewStudentDTO newStudentDTO = new NewStudentDTO();
                CompleteNameDTO nombreCompleto = new CompleteNameDTO("Alvaro", "Sanchez", "Hernandez");
                newStudentDTO.setNombreCompleto(nombreCompleto);
                newStudentDTO.setDni("12345671A");
                newStudentDTO.setMateriasMatriculadas(List.of());
                newStudentDTO.setIdInstituto(1L);
                newStudentDTO.setIdSede(1L);
                newStudentDTO.setNoEliminar(false);

                var peticionPut = put("http", "localhost", port, "/estudiantes/2", newStudentDTO);

                var respuestaPut = restTemplate.exchange(peticionPut, new ParameterizedTypeReference<StudentDTO>() {});
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(200);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(2);
                assertThat(respuestaPut.getBody().isNoEliminar());
            }

            @Test
            @DisplayName("Devuelve error 404 al modificar un estudiante que no existe")
            public void actualizaEstudianteQueNoExiste(){

                NewStudentDTO newStudentDTO = new NewStudentDTO();
                CompleteNameDTO nombreCompleto = new CompleteNameDTO("Alvaro", "Sanchez", "Hernandez");
                newStudentDTO.setNombreCompleto(nombreCompleto);
                newStudentDTO.setDni("12345678A");
                newStudentDTO.setMateriasMatriculadas(List.of());
                newStudentDTO.setIdInstituto(1L);
                newStudentDTO.setIdSede(1L);
                newStudentDTO.setNoEliminar(false);

                var peticionPut = put("http", "localhost", port, "/estudiantes/10", newStudentDTO);

                var respuestaPut = restTemplate.exchange(peticionPut, Void.class);
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(404);

                List<Student> estudiantesBD = studentRepo.findAll();
                assertThat(estudiantesBD).hasSize(1);
            }

            @Test
            @DisplayName("Modifica un instituto y devuelve 200")
            public void actualizaInstituto(){

                Institute instituto = new Institute();

                var peticionPut = put("http", "localhost", port, "/institutos/1", instituto);
                var respuestaPut = restTemplate.exchange(peticionPut, new ParameterizedTypeReference<Institute>() {
                });
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(200);

                List<Institute> institutosBD = instituteRepo.findAll();
                assertThat(institutosBD).hasSize(1);
                checkFields(instituto, institutosBD.get(0));
            }

            @Test
            @DisplayName("Devuelve error 404 al modificar un instituto que no existe")
            public void actualizaInstitutoQueNoExiste(){

                Institute instituto = new Institute();

                var peticionPut = put("http", "localhost", port, "/institutos/10", instituto);

                var respuestaPut = restTemplate.exchange(peticionPut, Void.class);
                assertThat(respuestaPut.getStatusCode().value()).isEqualTo(404);

                List<Institute> institutosBD = instituteRepo.findAll();

                assertThat(institutosBD).hasSize(1);
            }
        }

        @Test
        @DisplayName("Subir csv de estudiantes")
        public void subirCSV() {
            // Datos de ejemplo
            String csvData = "CENTRO;Nombre;Apellido1;Apellido2;DNI/NIF;DETALLE_MATERIAS\n" +
                    "IES;Paula;Gámez;Baños;88126719U;Matematicas";

            String baseUrl = "http://localhost:"+port+"/estudiantes/upload";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("ficheroEstudiantes", new ByteArrayResource(csvData.getBytes()) {
                @Override
                public String getFilename() {
                    return "estudiantes.csv"; // Nombre del archivo
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Establecer el tipo de contenido a multipart/form-data

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImportacionEstudiantesDTO> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ImportacionEstudiantesDTO>() {});

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            // Comprobamos que la lista de importados es no vacia
            assertThat(!(response.getBody().getImportados().isEmpty()));
            // Comprobamos que efectivamente tenemos 2 estudiantes en la base de datos
            assertThat(studentRepo.findAll()).hasSize(2);
        }

        @Test
        @DisplayName("Subir csv de estudiantes con DNI existente")
        public void subirCSVconDniExistente() {
            // Datos de ejemplo
            String csvData = "CENTRO;Nombre;Apellido1;Apellido2;DNI/NIF;DETALLE_MATERIAS\n" +
                    "IES;Paula;Gámez;Baños;12345678A;Matematicas";

            String baseUrl = "http://localhost:"+port+"/estudiantes/upload";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("ficheroEstudiantes", new ByteArrayResource(csvData.getBytes()) {
                @Override
                public String getFilename() {
                    return "estudiantes.csv"; // Nombre del archivo
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Establecer el tipo de contenido a multipart/form-data

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImportacionEstudiantesDTO> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ImportacionEstudiantesDTO>() {});

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            // Comprobamos que la lista de importados es no vacia
            assertThat(!(response.getBody().getNoImportados().isEmpty()));
        }

        @Test
        @DisplayName("Subir csv de estudiantes con Instituto no existente")
        public void subirCSVconInstitutoYMateriasNoExistente() {
            // Datos de ejemplo
            String csvData = "CENTRO;Nombre;Apellido1;Apellido2;DNI/NIF;DETALLE_MATERIAS\n" +
                    "IESNoExiste;Paula;Gámez;Baños;12345678A;MatematicasNoExiste";

            String baseUrl = "http://localhost:"+port+"/estudiantes/upload";

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("ficheroEstudiantes", new ByteArrayResource(csvData.getBytes()) {
                @Override
                public String getFilename() {
                    return "estudiantes.csv"; // Nombre del archivo
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Establecer el tipo de contenido a multipart/form-data

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImportacionEstudiantesDTO> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ImportacionEstudiantesDTO>() {});

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            // Comprobamos que la lista de importados es no vacia
            assertThat(!(response.getBody().getNoImportados().isEmpty()));
        }

    }
}