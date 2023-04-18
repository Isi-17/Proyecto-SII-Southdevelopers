package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.service.MateriaDBService;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/materias")
public class MateriaController {

    public MateriaDBService service;

    public MateriaController(MateriaDBService service) {
        this.service = service;
    }

    public static Function<Long, URI> materiaUriBuilder(UriComponents uriBuilder) {
        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/materias")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @GetMapping
    public List<Enrolment> obtainMaterias(UriComponentsBuilder uriBuilder) {
        var materias = service.allEnrolments();
        return materias.stream()
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> addMateria(@RequestBody Enrolment enrolment, UriComponentsBuilder uriBuilder) {
        Long id = service.addEnrolment(enrolment);
        return ResponseEntity.created(materiaUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

}
