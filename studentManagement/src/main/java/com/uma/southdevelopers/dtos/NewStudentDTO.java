package com.uma.southdevelopers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uma.southdevelopers.entities.Enrolment;
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


    public static NewStudentDTO fromStudent(Student student, Long idConvocatoria) {
        var dto = new NewStudentDTO();

        dto.setId(student.getId());
        CompleteNameDTO completeNameDTO = new CompleteNameDTO(student.getNombre(), student.getApellido1(), student.getApellido2());
        dto.setNombreCompleto(completeNameDTO);
        dto.setDni(student.getDni());
        dto.setTelefono(student.getTelefono());
        dto.setEmail(student.getEmail());
        List<Long> materias = new ArrayList<>();
        List<Enrolment> matriculas = student.getMatriculas();
        for (Enrolment enrolment : matriculas) {
            if(idConvocatoria == null && enrolment.getIdConvocatoria() == 2023L) {
                for (Subject subject : enrolment.getMateriasMatriculadas()){
                    materias.add(subject.getId());
                }
            } else if (idConvocatoria != null && enrolment.getIdConvocatoria() == idConvocatoria) {
                for (Subject subject : enrolment.getMateriasMatriculadas()){
                    materias.add(subject.getId());
                }
            }
        }
        dto.setMateriasMatriculadas(materias);
        dto.setIdSede(student.getIdSede());
        dto.setIdInstituto(student.getInstituto().getId());
        dto.setNoEliminar(student.isNoEliminar());

        return dto;
    }

    public Student student(Institute institute, List<Enrolment> matriculas) {
        var stud = new Student();

        stud.setId(id);
        stud.setNombre(nombreCompleto.getNombre());
        stud.setApellido1(nombreCompleto.getApellido1());
        stud.setApellido2(nombreCompleto.getApellido2());
        stud.setDni(dni);
        stud.setTelefono(telefono);
        stud.setEmail(email);
        //List<Enrolment> matriculas = new ArrayList<>();
        //Enrolment matricula = new Enrolment();
        //matricula.setIdConvocatoria(2023L);
        //matricula.setMateriasMatriculadas(materiasMatriculadas);
        //matriculas.add(matricula);
        stud.setMatriculas(matriculas);
        stud.setIdSede(idSede);
        stud.setInstituto(institute);
        stud.setNoEliminar(noEliminar);

        return stud;

    }

}
