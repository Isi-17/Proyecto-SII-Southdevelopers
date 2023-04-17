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
public class NewStudent {

    @Id @GeneratedValue
    private Long Id;
    @OneToOne
    private CompleteName nombreCompleto;
    private String dni;
    private String telefono;
    private String email;
    @OneToMany
    private List<Enrolment> materiasMatriculadas;
    private Long idInstituto;
    private Long idSede;
    private boolean noEliminar;
}
