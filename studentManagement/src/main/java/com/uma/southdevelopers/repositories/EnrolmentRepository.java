package com.uma.southdevelopers.repositories;

import com.uma.southdevelopers.entities.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uma.southdevelopers.entities.Enrolment;

import java.util.Optional;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    Optional<Enrolment> findByNombre(String nombre);
}
