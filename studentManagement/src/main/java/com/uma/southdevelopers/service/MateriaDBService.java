package com.uma.southdevelopers.service;


import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.repositories.EnrolmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MateriaDBService {

    private EnrolmentRepository enrolmentRepository;

    @Autowired
    public MateriaDBService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    public List<Subject> allEnrolments() {
        return enrolmentRepository.findAll();
    }

    public Long addEnrolment(Subject subject) {
        subject.setId(null);
        enrolmentRepository.save(subject);
        return subject.getId();
    }

    public Optional<Subject> obtainMateria(String name) {
        return enrolmentRepository.findByNombre(name);
    }

    public Boolean checkMateria(String name) {
        var materia = enrolmentRepository.findByNombre(name);
        return (materia.isPresent());
    }

    public Optional<Subject> obtainMateria(Long id) {
        return enrolmentRepository.findById(id);
    }

}
