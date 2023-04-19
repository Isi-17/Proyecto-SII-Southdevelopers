package com.uma.southdevelopers.controllers;


import com.opencsv.CSVReader;
import com.uma.southdevelopers.dtos.InstituteDTO;
import com.uma.southdevelopers.dtos.StudentDTO;

import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.service.InstituteDBService;
import com.uma.southdevelopers.service.StudentDBService;
import com.uma.southdevelopers.service.exceptions.EntityDoNotDeleteException;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
import com.uma.southdevelopers.service.exceptions.ExistingEntityDniException;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/estudiantes")
public class StudentController {

    public StudentDBService service;
    public InstituteDBService serviceInstitute;

    public StudentController(StudentDBService service, InstituteDBService serviceInstitute) {
        this.service = service;
        this.serviceInstitute = serviceInstitute;
    }

    public static Function<Long, URI> studentUriBuilder(UriComponents uriBuilder) {
        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/estudiantes")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @GetMapping
    public List<StudentDTO> obtainStudents(UriComponentsBuilder uriBuilder) {
        var students = service.allStudents();
        Function<Student, StudentDTO> mapper = (p ->
                StudentDTO.fromStudent(p,
                        studentUriBuilder(uriBuilder.build())));
        return students.stream()
                .map(mapper)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentDTO student, UriComponentsBuilder uriBuilder) {
        Institute institute = serviceInstitute.obtainInstitute(student.getIdInstituto());

        Student stud = student.student(institute);

        Long id = service.addStudent(stud);

        return ResponseEntity.created(studentUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public StudentDTO obtainStudent(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        Student student = service.obtainStudent(id);
        return StudentDTO.fromStudent(student,
                studentUriBuilder(uriBuilder.build()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public void updateStudent(@PathVariable Long id, @RequestBody StudentDTO student) {
        Institute institute = serviceInstitute.obtainInstitute(student.getIdInstituto());
        Student entidadStudent = student.student(institute);
        entidadStudent.setId(id);
        service.updateStudent(entidadStudent);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }

    @PostMapping("/upload")
    public void uploadCSV(@RequestParam("ficheroEstudiantes") MultipartFile csvFile) throws Exception {




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
