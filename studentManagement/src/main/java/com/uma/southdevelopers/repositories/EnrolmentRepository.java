package com.uma.southdevelopers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uma.southdevelopers.entities.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
}
