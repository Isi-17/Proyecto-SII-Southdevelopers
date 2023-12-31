package com.uma.southdevelopers.repositories;

import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByDni(String dni);
    Optional<List<Student>> findAllByIdSede(Long idSede);
    Optional<List<Student>> findAllByInstituto(Institute instituto);
}
