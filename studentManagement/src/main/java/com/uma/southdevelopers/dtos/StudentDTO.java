package com.uma.southdevelopers.dtos;

import com.uma.southdevelopers.entities.Enrolment;
import com.uma.southdevelopers.entities.Institute;
import lombok.*;

import java.util.List;

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
    private List<Enrolment> materiasMatriculadas;
    private Long idSede;
    private Institute instituto;
    private boolean noEliminar;




}
