package com.uma.southdevelopers.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Student {
    @Id @GeneratedValue
    private Long Id;
    @OneToOne
    private CompleteName nombre;
    private String dni;
    private String telefono;
    private String email;
    @OneToMany
    private List<Enrolment> materiasMatriculadas;
    private Long idSede;
    @OneToOne
    private Institute instituto;
    private boolean noEliminar;

}
