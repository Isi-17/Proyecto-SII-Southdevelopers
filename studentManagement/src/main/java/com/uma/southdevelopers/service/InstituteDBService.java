package com.uma.southdevelopers.service;


import com.uma.southdevelopers.controllers.InstituteController;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.repositories.InstituteRepository;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
@Transactional
public class InstituteDBService {

    private InstituteRepository instituteRepository;

    @Autowired
    public InstituteDBService(InstituteRepository repo) {
        this.instituteRepository = repo;
    }

    public List<Institute> allInstitutes() {
        return instituteRepository.findAll();
    }

    public Long addInstitute(Institute institute) {
        institute.setId(null);
        instituteRepository.save(institute);
        return institute.getId();
    }

    public Institute obtainInstitute(Long id) {
        var institute = instituteRepository.findById(id);
        if (institute.isPresent()) {
            return institute.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Institute obtainInstitute(String name) {
        var institute = instituteRepository.findByNombre(name);
        if (institute.isPresent()) {
            return institute.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Boolean checkInstitute(String name) {
        var institute = instituteRepository.findByNombre(name);
        return (institute.isPresent());
    }

    public void updateInstitute(Institute institute) {
        if (instituteRepository.existsById(institute.getId())) {
            instituteRepository.save(institute);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void deleteInstitute(Long id) {
        if (instituteRepository.existsById(id)) {
            instituteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
