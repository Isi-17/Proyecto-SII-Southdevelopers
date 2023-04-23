package com.uma.southdevelopers.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrolment {

    @Id @GeneratedValue
    private Long id;
    private Long idConvocatoria;
    @ManyToMany
    private List<Subject> materiasMatriculadas;
}
