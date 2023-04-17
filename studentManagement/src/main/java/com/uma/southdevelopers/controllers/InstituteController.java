package com.uma.southdevelopers.controllers;


import com.uma.southdevelopers.dtos.InstituteDTO;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.service.InstituteDBService;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
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

    public InstituteController(InstituteDBService service) {
        this.service = service;
    }

    public static Function<Long, URI> instituteUriBuilder(UriComponents uriBuilder) {
        return id -> UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/institutes")
                .path(String.format("/%d", id))
                .build()
                .toUri();
    }

    @GetMapping
    public List<InstituteDTO> obtainInstitutes(UriComponentsBuilder uriBuilder) {
        var institutes = service.allInstitutes();
        Function<Institute, InstituteDTO> mapper = (p ->
                InstituteDTO.fromInstitute(p,
                        instituteUriBuilder(uriBuilder.build())));
        return institutes.stream()
                .map(mapper)
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> addInstitute(@RequestBody InstituteDTO institute, UriComponentsBuilder uriBuilder) {
        Institute inst = institute.institute();
        Long id = service.addInstitute(inst);
        return ResponseEntity.created(
                        instituteUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public InstituteDTO obtainInstitute(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        Institute institute = service.obtainInstitute(id);
        return InstituteDTO.fromInstitute(institute,
                instituteUriBuilder(uriBuilder.build()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(code=HttpStatus.OK)
    public void updateInstitute(@PathVariable Long id, @RequestBody InstituteDTO institute) {
        Institute entidadInstituto = institute.institute();
        entidadInstituto.setId(id);
        service.updateInstitute(entidadInstituto);
    }

    @DeleteMapping("{id}")
    public void deleteInstitute(@PathVariable Long id) {
        service.deleteInstitute(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void notFound() {}

}
