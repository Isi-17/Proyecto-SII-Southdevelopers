package com.uma.southdevelopers.dtos;

import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Institute;
import com.uma.southdevelopers.entities.Student;
import com.uma.southdevelopers.entities.Subject;
import lombok.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private Long id;
    private CompleteNameDTO nombreCompleto;
    private String dni;
    private String telefono;
    private String email;
    private List<Subject> materiasMatriculadas;
    private Long idSede;
    private Institute instituto;
    private boolean noEliminar;

    public static StudentDTO fromStudent(Student student, Long idConvocatoria) {
        var dto = new StudentDTO();

        dto.setId(student.getId());
        CompleteNameDTO completeNameDTO = new CompleteNameDTO(student.getNombre(), student.getApellido1(), student.getApellido2());
        dto.setNombreCompleto(completeNameDTO);
        dto.setDni(student.getDni());
        dto.setTelefono(student.getTelefono());
        dto.setEmail(student.getEmail());


        List<Enrolment> matriculas = student.getMatriculas();
        for (Enrolment enrolment : matriculas) {
            if(enrolment.getIdConvocatoria().equals(idConvocatoria)) {
                dto.setMateriasMatriculadas(enrolment.getMateriasMatriculadas());
            }
        }

        dto.setIdSede(student.getIdSede());
        dto.setInstituto(student.getInstituto());
        dto.setNoEliminar(student.isNoEliminar());

        return dto;
    }

}
