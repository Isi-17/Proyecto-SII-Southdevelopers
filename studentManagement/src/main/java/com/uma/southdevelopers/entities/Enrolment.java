package com.uma.southdevelopers.entities;

import jakarta.persistence.*;
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
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> materiasMatriculadas;
}
