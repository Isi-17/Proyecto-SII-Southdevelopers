package com.uma.southdevelopers.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {

    enum Rol {
        ESTUDIANTE, RESPONSABLE_SEDE, VIGILANTE_AULA, CORRECTOR, ADMINISTRADOR
    }

    @Id @GeneratedValue
    private Long userId;

    private String email;

    @NonNull
    private String name;

    private String surname;

    @NonNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

}
