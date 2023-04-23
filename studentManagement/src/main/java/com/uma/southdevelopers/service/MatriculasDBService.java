package com.uma.southdevelopers.service;


import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.repositories.EnrolmentRepository;
import com.uma.southdevelopers.repositories.MatriculasRepository;
import com.uma.southdevelopers.service.exceptions.EntityDoNotDeleteException;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatriculasDBService {

    private MatriculasRepository matriculasRepository;

    @Autowired
    public MatriculasDBService(MatriculasRepository matriculasRepository) {
        this.matriculasRepository = matriculasRepository;
    }

    public List<Enrolment> allMatriculas() {
        return matriculasRepository.findAll();
    }

    public Long addMatriculas(Enrolment enrolment) {
        enrolment.setId(null);
        matriculasRepository.save(enrolment);
        return enrolment.getId();
    }

}
