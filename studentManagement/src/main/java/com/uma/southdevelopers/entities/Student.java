package com.uma.southdevelopers.entities;


import com.uma.southdevelopers.dtos.StudentDTO;
import com.uma.southdevelopers.dtos.CompleteNameDTO;
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
    private List<Enrolment> matriculas;
    private Long idSede;
    @OneToOne
    private Institute instituto;
    private boolean noEliminar;

    public StudentDTO toDTO(){

        return new StudentDTO(this.Id,
                new CompleteNameDTO(this.nombre, this.apellido1, this.apellido2),
                this.dni,
                this.telefono,
                this.email,
                this.matriculas,
                this.idSede,
                this.instituto,
                this.noEliminar);
    }

}
