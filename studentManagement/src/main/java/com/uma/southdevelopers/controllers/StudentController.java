package com.uma.southdevelopers.controllers;


import com.uma.southdevelopers.dtos.ImportacionEstudiantesDTO;
import com.uma.southdevelopers.dtos.NewStudentDTO;

import com.uma.southdevelopers.dtos.StudentDTO;
import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.service.InstituteDBService;
import com.uma.southdevelopers.service.MateriaDBService;
import com.uma.southdevelopers.service.MatriculasDBService;
import com.uma.southdevelopers.service.StudentDBService;
import com.uma.southdevelopers.service.exceptions.EntityDoNotDeleteException;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
import com.uma.southdevelopers.service.exceptions.ExistingEntityDniException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/estudiantes")
public class StudentController {

    public StudentDBService service;
    public InstituteDBService serviceInstitute;
    public MateriaDBService serviceMateria;

    public MatriculasDBService serviceMatriculas;

    public StudentController(StudentDBService service,
                             InstituteDBService serviceInstitute,
                             MateriaDBService serviceMateria,
                             MatriculasDBService serviceMatriculas) {
        this.service = service;
        this.serviceInstitute = serviceInstitute;
        this.serviceMateria = serviceMateria;
        this.serviceMatriculas = serviceMatriculas;
    }

    public static Function<Long, URI> studentUriBuilder(UriComponents uriBuilder) {
        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/estudiantes")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @GetMapping
    public List<NewStudentDTO> obtainStudents(@RequestParam(value = "idSede", required = false) Long idSede,
                                              @RequestParam(value = "idConvocatoria", required = false) Long idConvocatoria,
                                              UriComponentsBuilder uriBuilder) {
        var students = (idSede==null) ? service.allStudents() : service.obtainStudentFromSede(idSede);
        Function<Student, NewStudentDTO> mapper = (p ->
                NewStudentDTO.fromStudent(p, idConvocatoria, studentUriBuilder(uriBuilder.build())));
        return students.stream()
                .map(mapper)
                .toList();
    }

    @PostMapping
    @ResponseStatus(code=HttpStatus.CREATED)
    public StudentDTO addStudent(@RequestBody NewStudentDTO student, UriComponentsBuilder uriBuilder) {
        Institute institute = serviceInstitute.obtainInstitute(student.getIdInstituto());

        List<Subject> materias = new ArrayList<>();
        List<Long> materiasFromStudentDTO = student.getMateriasMatriculadas();

        for(int i = 0; i < materiasFromStudentDTO.size(); i++) {
            Optional<Subject> materia = serviceMateria.obtainMateria(materiasFromStudentDTO.get(i));
            if (materia.isPresent()) {
                materias.add(materia.get());
            } else {
                throw new EntityNotFoundException();
            }
        }
        List<Enrolment> matriculas = new ArrayList<>();
        Enrolment matricula = new Enrolment();
        matricula.setIdConvocatoria(2023L);
        matricula.setMateriasMatriculadas(materias);
        matriculas.add(matricula);

        serviceMatriculas.addMatriculas(matricula);

        Student stud = student.student(institute, matriculas);

        Long id = service.addStudent(stud);

        return stud.toDTO();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public StudentDTO obtainStudent(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        Student student = service.obtainStudent(id);
        return StudentDTO.fromStudent(student, 2023L,
                studentUriBuilder(uriBuilder.build()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public StudentDTO updateStudent(@PathVariable Long id, @RequestBody NewStudentDTO student) {
        Institute institute = serviceInstitute.obtainInstitute(student.getIdInstituto());

        List<Subject> materias = new ArrayList<>();
        List<Long> materiasFromStudentDTO = student.getMateriasMatriculadas();

        for (int i = 0; i < materiasFromStudentDTO.size(); i++) {
            Optional<Subject> materia = serviceMateria.obtainMateria(materiasFromStudentDTO.get(i));
            if (materia.isPresent()) {
                materias.add(materia.get());
                System.out.println(materia.get());
            } else {
                throw new EntityNotFoundException();
            }
        }

        List<Enrolment> matriculas = new ArrayList<>();
        Enrolment matricula = new Enrolment();
        matricula.setIdConvocatoria(2023L);
        matricula.setMateriasMatriculadas(materias);
        matriculas.add(matricula);

        serviceMatriculas.addMatriculas(matricula);

        Student entidadStudent = student.student(institute, matriculas);
        entidadStudent.setId(id);
        service.updateStudent(entidadStudent);
        return entidadStudent.toDTO();
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }

    @PostMapping("/upload")
    public ImportacionEstudiantesDTO uploadCSV(@RequestParam("ficheroEstudiantes") MultipartFile csvFile) throws Exception {

        ImportacionEstudiantesDTO importacionEstudiantesDTO = new ImportacionEstudiantesDTO();

        InputStreamReader in = new InputStreamReader(new BOMInputStream(csvFile.getInputStream()), "UTF-8");

        try (BufferedReader fileReader = new BufferedReader(in);
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader());) {

            List<Student> tutorials = new ArrayList<Student>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            int i = 0;
            for (CSVRecord csvRecord : csvRecords) {
                Student student = new Student();
                student.setNombre(csvRecord.get("Nombre"));
                student.setApellido1(csvRecord.get("Apellido1"));
                student.setApellido2(csvRecord.get("Apellido2"));
                student.setDni(csvRecord.get("DNI/NIF"));
                Institute institute = serviceInstitute.obtainInstitute(csvRecord.get("CENTRO"));
                student.setInstituto(institute);

                String[] materias = csvRecord.get("DETALLE_MATERIAS").split(",");
                List<Subject> subjects = new ArrayList<>();
                for(int j = 0; j < materias.length; j++) {
                    Optional<Subject> enrolment = serviceMateria.obtainMateria(materias[j]);
                    if (!enrolment.isPresent()) {
                        serviceMateria.addEnrolment(new Subject(0L, materias[j], true));
                    }
                    enrolment = serviceMateria.obtainMateria(materias[j]);
                    subjects.add(enrolment.get());

                }

                List<Enrolment> matriculas = new ArrayList<>();

                Enrolment matricula = new Enrolment();
                matricula.setIdConvocatoria(2023L);
                matricula.setMateriasMatriculadas(subjects);

                matriculas.add(matricula);

                student.setMatriculas(matriculas);

                service.addStudent(student);
                importacionEstudiantesDTO.addStudent(student.toDTO());

                // Codigo para parar la importacion
                // COMENTAR PARA IMPORTAR TODOS LOS REGISTROS
                i ++;
                if(i == 10){
                    break;
                }

            }

            return importacionEstudiantesDTO;
        }
    }

    @ExceptionHandler(ExistingEntityDniException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existingDni() {}

    @ExceptionHandler(EntityDoNotDeleteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void doNotDelete() {}

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void notFound() {}
}
