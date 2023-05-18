package com.uma.southdevelopers.repositories;

import com.uma.southdevelopers.entities.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstituteRepository extends JpaRepository<Institute, Long> {
    Optional<Institute> findByNombre(String nombre);
}
