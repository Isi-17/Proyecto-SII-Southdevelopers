package com.uma.southdevelopers.controllers;

import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.service.MateriaDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<Subject> obtainMaterias(UriComponentsBuilder uriBuilder) {
        var materias = service.allEnrolments();
        return materias.stream()
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> addMateria(@RequestBody Subject subject, UriComponentsBuilder uriBuilder) {
        Long id = service.addEnrolment(subject);
        return ResponseEntity.created(materiaUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    List<String> materias = Arrays.asList("Lengua Castellana y Literatura",
            "Historia de España",
            "Inglés (Fase de Acceso)",
            "Francés (Fase de Acceso)",
            "Alemán (Fase de Acceso)",
            "Italiano (Fase de Acceso)",
            "Portugués (Fase de Acceso)",
            "Inglés (Fase de Admisión)",
            "Francés (Fase de Admisión)",
            "Alemán (Fase de Admisión)",
            "Italiano (Fase de Admisión)",
            "Portugués (Fase de Admisión)",
            "Fundamentos del Arte II",
            "Latín II",
            "Matemáticas II",
            "Matemáticas Aplicadas a las CCSS",
            "Artes Escénicas",
            "Biología",
            "Cultura Audiovisual II",
            "Dibujo Técnico II",
            "Diseño",
            "Economía de la Empresa",
            "Física",
            "Geografía",
            "Geología",
            "Griego II",
            "Historia de la Filosofía",
            "Historia del Arte",
            "Matematicas II",
            "Química");


    @PostMapping(path = "/test")
    public void addMaterias() {
        for (String materia : materias) {
            service.addEnrolment(new Subject(0L, materia, true));
        }
    }

}
