package com.uma.southdevelopers.service;


import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MateriaDBService {

    private SubjectRepository subjectRepository;

    @Autowired
    public MateriaDBService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> allEnrolments() {
        return subjectRepository.findAll();
    }

    public Long addEnrolment(Subject subject) {
        subject.setId(null);
        subjectRepository.save(subject);
        return subject.getId();
    }

    public Optional<Subject> obtainMateria(String name) {
        return subjectRepository.findByNombre(name);
    }

    public Boolean checkMateria(String name) {
        var materia = subjectRepository.findByNombre(name);
        return (materia.isPresent());
    }

    public Optional<Subject> obtainMateria(Long id) {
        return subjectRepository.findById(id);
    }

}
