package com.uma.southdevelopers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uma.southdevelopers.entities.SpecialNeeds;
@Repository
public interface SpecialNeedsRepository extends JpaRepository<SpecialNeeds, Long> {
}
