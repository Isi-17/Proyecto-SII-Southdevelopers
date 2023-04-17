package com.uma.southdevelopers.controllers;


import com.uma.southdevelopers.dtos.StudentDTO;

import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.NewStudent;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.service.InstituteDBService;
import com.uma.southdevelopers.service.StudentDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<?> addStudent(@RequestBody NewStudent student, UriComponentsBuilder uriBuilder) {
        System.out.println(student);
        System.out.println("HOLA");
        Student stud = student.student();
        System.out.println(student.getIdInstituto());
        System.out.println(serviceInstitute.obtainInstitute(student.getIdInstituto()));
        stud.setInstituto(serviceInstitute.obtainInstitute(student.getIdInstituto()));
        Long id = service.addStudent(stud);
        System.out.println("ID: " + id);
        return ResponseEntity.created(studentUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }
}
