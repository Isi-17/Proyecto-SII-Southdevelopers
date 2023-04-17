package com.uma.southdevelopers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uma.southdevelopers.entities.CompleteName;
import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import lombok.*;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private CompleteName nombre;
    private Long id;
    private String dni;
    private String telefono;
    private String email;
    private List<Enrolment> materiasMatriculadas;
    private Long idSede;
    private Institute instituto;
    private boolean noEliminar;
    @JsonProperty("_links")
    private Links links;

    public static StudentDTO fromStudent(Student student,
                                         Function<Long, URI> studentUriBuilder) {
        var dto = new StudentDTO();

        dto.setId(student.getId());
        CompleteName completeName = new CompleteName(student.getNombre(), student.getApellido1(), student.getApellido2());
        dto.setNombre(completeName);
        dto.setDni(student.getDni());
        dto.setTelefono(student.getTelefono());
        dto.setEmail(student.getEmail());
        dto.setMateriasMatriculadas(student.getMateriasMatriculadas());
        dto.setIdSede(student.getIdSede());
        dto.setInstituto(student.getInstituto());
        dto.setNoEliminar(student.isNoEliminar());
        dto.setLinks(Links.builder()
                .self(studentUriBuilder.apply(student.getId()))
                .build());

        return dto;
    }

    public Student student() {
        var stud = new Student();

        stud.setId(id);
        stud.setNombre(nombre.getNombre());
        stud.setApellido1(nombre.getApellido1());
        stud.setApellido2(nombre.getApellido2());
        stud.setDni(dni);
        stud.setTelefono(telefono);
        stud.setEmail(email);
        stud.setMateriasMatriculadas(materiasMatriculadas);
        stud.setIdSede(idSede);
        stud.setInstituto(instituto);
        stud.setNoEliminar(noEliminar);

        return stud;

    }

}
