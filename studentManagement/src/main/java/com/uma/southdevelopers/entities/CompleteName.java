package com.uma.southdevelopers.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CompleteName {

    @Id
    private Long id;
    private String surname1;
    private String surname2;
    private String name;
}
