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
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String dni;
    private String telefono;
    private String email;
    @ManyToMany
    private List<Enrolment> materiasMatriculadas;
    private Long idSede;
    @OneToOne
    private Institute instituto;
    private boolean noEliminar;

}
