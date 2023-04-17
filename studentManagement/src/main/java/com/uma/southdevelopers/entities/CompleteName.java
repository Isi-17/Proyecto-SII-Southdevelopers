package com.uma.southdevelopers.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CompleteName {

    @Id
    private Long id;
    private String apellido1;
    private String apellido2;
    private String nombre;
}
