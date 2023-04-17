package com.uma.southdevelopers.service;

import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.repositories.EnrolmentRepository;
import com.uma.southdevelopers.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentDBService {

    private StudentRepository studentRepository;
    private EnrolmentRepository enrolmentRepository;

    @Autowired
    public StudentDBService(StudentRepository repo, EnrolmentRepository enrolmentRepository) {
        this.studentRepository = repo;
        this.enrolmentRepository = enrolmentRepository;
    }

    public List<String> allStudents() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getDni)
                .toList();
    }

}
