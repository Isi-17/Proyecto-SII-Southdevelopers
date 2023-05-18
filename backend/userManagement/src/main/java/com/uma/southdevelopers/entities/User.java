package com.uma.southdevelopers.entities;


import lombok.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Table(name = "_user")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public enum Role {
        RESPONSABLE_SEDE, VIGILANTE_AULA, CORRECTOR, VICERRECTORADO
    }

    @Id @GeneratedValue
    private Long userId;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    private String surname1;

    private String surname2;

    @Column(nullable = false)
    private String password;

    @ElementCollection @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

}
