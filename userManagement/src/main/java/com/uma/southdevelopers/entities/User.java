package com.uma.southdevelopers.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {

    public enum Role {
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

    @ElementCollection(fetch = FetchType.EAGER) @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

}
