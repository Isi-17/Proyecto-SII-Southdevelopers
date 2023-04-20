package com.uma.southdevelopers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uma.southdevelopers.entities.Subject;

import java.util.Optional;

@Repository
public interface EnrolmentRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByNombre(String nombre);
}
