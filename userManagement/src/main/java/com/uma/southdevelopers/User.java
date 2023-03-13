package com.uma.southdevelopers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    enum Rol {
        ESTUDIANTE, RESPONSABLE_SEDE, VIGILANTE_AULA, CORRECTOR, ADMINISTRADOR
    }

    @Id @GeneratedValue
    private Long userId;
    private String email;
    private String name;
    private String surname;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;

}
