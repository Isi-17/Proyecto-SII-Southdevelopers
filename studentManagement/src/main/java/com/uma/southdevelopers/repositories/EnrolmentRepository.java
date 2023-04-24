package com.uma.southdevelopers.repositories;

import com.uma.southdevelopers.entities.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
}
