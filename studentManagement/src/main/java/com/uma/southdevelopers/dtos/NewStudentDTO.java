package com.uma.southdevelopers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uma.southdevelopers.entities.Subject;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import lombok.*;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewStudentDTO {

    private CompleteNameDTO nombreCompleto;
    private Long id;
    private String dni;
    private String telefono;
    private String email;
    private List<Long> materiasMatriculadas;
    private Long idSede;
    private Long idInstituto;
    private boolean noEliminar;
    @JsonProperty("_links")
    private Links links;

    public static NewStudentDTO fromStudent(Student student,
                                            Function<Long, URI> studentUriBuilder) {
        var dto = new NewStudentDTO();

        dto.setId(student.getId());
        CompleteNameDTO completeNameDTO = new CompleteNameDTO(student.getNombre(), student.getApellido1(), student.getApellido2());
        dto.setNombreCompleto(completeNameDTO);
        dto.setDni(student.getDni());
        dto.setTelefono(student.getTelefono());
        dto.setEmail(student.getEmail());
        List<Long> materias = new ArrayList<>();
        for (Subject subject : student.getMatriculas()) {
            materias.add(subject.getId());
        }
        dto.setMateriasMatriculadas(materias);
        dto.setIdSede(student.getIdSede());
        dto.setIdInstituto(student.getInstituto().getId());
        dto.setNoEliminar(student.isNoEliminar());
        dto.setLinks(Links.builder()
                .self(studentUriBuilder.apply(student.getId()))
                .build());

        return dto;
    }

    public Student student(Institute institute, List<Subject> materiasMatriculadas) {
        var stud = new Student();

        stud.setId(id);
        stud.setNombre(nombreCompleto.getNombre());
        stud.setApellido1(nombreCompleto.getApellido1());
        stud.setApellido2(nombreCompleto.getApellido2());
        stud.setDni(dni);
        stud.setTelefono(telefono);
        stud.setEmail(email);
        stud.setMatriculas(materiasMatriculadas);
        stud.setIdSede(idSede);
        stud.setInstituto(institute);
        stud.setNoEliminar(noEliminar);

        return stud;

    }

}
