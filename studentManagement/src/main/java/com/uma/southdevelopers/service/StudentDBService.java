package com.uma.southdevelopers.service;

import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.repositories.EnrolmentRepository;
import com.uma.southdevelopers.repositories.InstituteRepository;
import com.uma.southdevelopers.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentDBService {

    private StudentRepository studentRepository;
    private InstituteRepository instituteRepository;
    private EnrolmentRepository enrolmentRepository;

    @Autowired
    public StudentDBService(StudentRepository repo,
                            EnrolmentRepository enrolmentRepository,
                            InstituteRepository instituteRepository) {
        this.studentRepository = repo;
        this.instituteRepository = instituteRepository;
        this.enrolmentRepository = enrolmentRepository;
    }

    public List<Student> allStudents() {
        return studentRepository.findAll();
    }

    public Long addStudent(Student student) {
        student.setId(null);
        System.out.println(student);
        studentRepository.save(student);
        return student.getId();
    }

}
