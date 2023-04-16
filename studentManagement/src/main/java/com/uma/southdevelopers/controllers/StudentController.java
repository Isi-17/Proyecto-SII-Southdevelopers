package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.service.StudentDBService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/estudiantes")
public class StudentController {

    public StudentDBService service;

    public StudentController(StudentDBService service) {
        this.service = service;
    }

    @GetMapping
    public List<String> obtainStudents() {
        return service.allStudents();
    }
}
