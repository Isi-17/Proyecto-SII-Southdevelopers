package com.uma.southdevelopers.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemaImportacionDTO {

    private StudentDTO estudiante;
    private String problemaImportacion;

}
