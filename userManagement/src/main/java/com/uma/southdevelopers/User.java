package com.uma.southdevelopers;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    enum Rol {
        ESTUDIANTE, RESPONSABLE_SEDE, VIGILANTE_AULA, CORRECTOR, ADMINISTRADOR
    }

    @Id @GeneratedValue
    private Long userId;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;

}
