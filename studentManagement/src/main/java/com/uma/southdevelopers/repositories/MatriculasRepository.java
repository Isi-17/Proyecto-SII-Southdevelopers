package com.uma.southdevelopers.repositories;

import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatriculasRepository extends JpaRepository<Enrolment, Long> {
}
