package com.uma.southdevelopers.controllers;


import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.service.InstituteDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.net.URI;
@RestController
@RequestMapping(path = "/institutos")
public class InstituteController {

    public InstituteDBService service;

    public InstituteController(InstituteDBService service) {
        this.service = service;
    }

    @GetMapping
    public List<Institute> obtainInstitutes() {
        return service.allInstitutes().stream().toList();
    }

    @PostMapping
    public ResponseEntity<?> addInstitute(@RequestBody Institute institute, UriComponentsBuilder builder) {
        Long id = service.addInstitute(institute);
        URI uri = builder.path("/institutos")
                .path(String.format("/%d", id))
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }

}
