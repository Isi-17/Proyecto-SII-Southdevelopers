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
    private Long id;
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

    public Student student() {
        var stud = new Student();

        stud.setId(id);
        stud.setNombre(nombreCompleto.getNombre());
        stud.setApellido1(nombreCompleto.getApellido1());
        stud.setApellido2(nombreCompleto.getApellido2());
        stud.setDni(dni);
        stud.setTelefono(telefono);
        stud.setEmail(email);
        stud.setMateriasMatriculadas(materiasMatriculadas);
        stud.setIdSede(idSede);
        stud.setInstituto(null);
        stud.setNoEliminar(noEliminar);

        return stud;

    }
}
