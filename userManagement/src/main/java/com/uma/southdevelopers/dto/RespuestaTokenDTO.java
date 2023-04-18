package com.uma.southdevelopers.dto;

import lombok.Data;

@Data
public class RespuestaTokenDTO {
    private String jwt;

    public RespuestaTokenDTO(String jwt){
        this.jwt = jwt;
    }
}
