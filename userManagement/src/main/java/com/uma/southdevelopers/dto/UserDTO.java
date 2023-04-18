package com.uma.southdevelopers.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
}
