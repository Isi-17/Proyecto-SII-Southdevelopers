package com.uma.southdevelopers.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportacionEstudiantesDTO {

    List<StudentDTO> importados = new ArrayList<>();
    List<ProblemaImportacionDTO> noImportados = new ArrayList<>();

    public void addStudent(StudentDTO studentDTO) {
        importados.add(studentDTO);
    }

    public void addProblem(ProblemaImportacionDTO problemaImportacionDTO) {
        noImportados.add(problemaImportacionDTO);
    }

}
