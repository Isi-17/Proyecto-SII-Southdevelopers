package com.uma.southdevelopers.service;


import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.repositories.EnrolmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MatriculasDBService {

    private EnrolmentRepository enrolmentRepository;

    @Autowired
    public MatriculasDBService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    public Long addMatriculas(Enrolment enrolment) {
        enrolment.setId(null);
        enrolmentRepository.save(enrolment);
        return enrolment.getId();
    }

}
