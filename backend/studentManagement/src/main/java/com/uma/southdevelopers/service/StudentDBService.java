package com.uma.southdevelopers.service;

import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.repositories.SubjectRepository;
import com.uma.southdevelopers.repositories.InstituteRepository;
import com.uma.southdevelopers.repositories.StudentRepository;
import com.uma.southdevelopers.service.exceptions.EntityDoNotDeleteException;
import com.uma.southdevelopers.service.exceptions.EntityNotFoundException;
import com.uma.southdevelopers.service.exceptions.ExistingEntityDniException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentDBService {

    private StudentRepository studentRepository;
    private InstituteRepository instituteRepository;
    private SubjectRepository subjectRepository;

    @Autowired
    public StudentDBService(StudentRepository repo,
                            SubjectRepository subjectRepository,
                            InstituteRepository instituteRepository) {
        this.studentRepository = repo;
        this.instituteRepository = instituteRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Student> allStudents() {
        return studentRepository.findAll();
    }

    public List<Student> obtainStudentFromSede(Long idSede) {
        var studentOptional = studentRepository.findAllByIdSede(idSede);
        if(studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public List<Student> obtainStudentFromInstitute(Institute institute) {
        var studentOptional = studentRepository.findAllByInstituto(institute);
        if(studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Long addStudent(Student student) {
        var studentOptional = studentRepository.findByDni(student.getDni());
        if (studentOptional.isPresent()) {
            throw new ExistingEntityDniException();
        } else {
            student.setId(null);
            studentRepository.save(student);
            return student.getId();
        }
    }

    public Student obtainStudent(Long id) {
        var student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Boolean checkStudent(String dni) {
        var student = studentRepository.findByDni(dni);
        return (student.isPresent());
    }

    public void updateStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            var studentOptional = studentRepository.findByDni(student.getDni());
            if (!(studentOptional.isPresent() && !studentOptional.get().getId().equals(student.getId()))) {
                Student studentFromDB = studentRepository.findById(student.getId()).get();
                if(studentFromDB.isNoEliminar() && !student.isNoEliminar()) {
                    student.setNoEliminar(true);
                    studentRepository.save(student);
                } else {
                    studentRepository.save(student);
                }
            } else {
                throw new ExistingEntityDniException();
            }
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            var student = studentRepository.findById(id);
            if(student.isPresent() && !student.get().isNoEliminar()) {
                studentRepository.deleteById(id);
            } else {
                throw new EntityDoNotDeleteException();
            }
        } else {
            throw new EntityNotFoundException();
        }
    }

}
