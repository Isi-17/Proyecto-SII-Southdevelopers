package com.uma.southdevelopers.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrolment {

    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private Boolean eliminada;
    @ManyToOne
    private Student estudiante;

}
