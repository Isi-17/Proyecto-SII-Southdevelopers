package com.uma.southdevelopers.controllers;


import com.uma.southdevelopers.dtos.InstituteDTO;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.service.InstituteDBService;
import com.uma.southdevelopers.service.StudentDBService;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
import com.uma.southdevelopers.service.exceptions.InstitutoDoNotDeleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.net.URI;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/institutos")
public class InstituteController {

    public InstituteDBService service;
    public StudentDBService studentService;

    public InstituteController(InstituteDBService service, StudentDBService studentService) {
        this.service = service;
        this.studentService = studentService;
    }

    @GetMapping
    public List<InstituteDTO> obtainInstitutes() {
        var institutes = service.allInstitutes();
        Function<Institute, InstituteDTO> mapper = (p ->
                InstituteDTO.fromInstitute(p));
        return institutes.stream()
                .map(mapper)
                .toList();
    }

    @PostMapping
    @ResponseStatus(code=HttpStatus.CREATED)
    public Institute addInstitute(@RequestBody InstituteDTO institute, UriComponentsBuilder uriBuilder) {
        Institute inst = institute.institute();
        Long id = service.addInstitute(inst);
        inst.setId(id);
        return inst;
    }

    @GetMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public InstituteDTO obtainInstitute(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        Institute institute = service.obtainInstitute(id);
        return InstituteDTO.fromInstitute(institute);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public Institute updateInstitute(@PathVariable Long id, @RequestBody InstituteDTO institute) {
        Institute entidadInstituto = institute.institute();
        entidadInstituto.setId(id);
        service.updateInstitute(entidadInstituto);
        return entidadInstituto;
    }

    @DeleteMapping("/{id}")
    public void deleteInstitute(@PathVariable Long id) {
        Institute institute = service.obtainInstitute(id);

        List<Student> students = studentService.obtainStudentFromInstitute(institute);

        if(students.size() > 0) {
            throw new InstitutoDoNotDeleteException();
        }

        service.deleteInstitute(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void notFound() {}

    @ExceptionHandler(InstitutoDoNotDeleteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void instituteWithStudents() {}

}
