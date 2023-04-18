package com.uma.southdevelopers.service;


import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.repositories.EnrolmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MateriaDBService {

    private EnrolmentRepository enrolmentRepository;

    @Autowired
    public MateriaDBService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    public List<Enrolment> allEnrolments() {
        return enrolmentRepository.findAll();
    }

    public Long addEnrolment(Enrolment enrolment) {
        enrolment.setId(null);
        enrolmentRepository.save(enrolment);
        return enrolment.getId();
    }
}
