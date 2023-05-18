package com.uma.southdevelopers.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.uma.southdevelopers.entities.Institute;
import lombok.*;

import java.net.URI;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstituteDTO {
    private Long id;
    private String nombre;
    private String direccion1;
    private String direccion2;
    private String localidad;
    private Integer codigoPostal;
    private String pais;


    public static InstituteDTO fromInstitute(Institute institute) {
        var dto = new InstituteDTO();

        dto.setId(institute.getId());
        dto.setNombre(institute.getNombre());
        dto.setDireccion1(institute.getDireccion1());
        dto.setDireccion2(institute.getDireccion2());
        dto.setLocalidad(institute.getLocalidad());
        dto.setCodigoPostal(institute.getCodigoPostal());
        dto.setPais(institute.getPais());

        return dto;
    }

    public Institute institute() {
        var inst = new Institute();

        inst.setId(id);
        inst.setNombre(nombre);
        inst.setDireccion1(direccion1);
        inst.setDireccion2(direccion2);
        inst.setLocalidad(localidad);
        inst.setCodigoPostal(codigoPostal);
        inst.setPais(pais);

        return inst;
    }
}
