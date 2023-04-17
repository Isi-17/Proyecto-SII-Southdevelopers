package com.uma.southdevelopers.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrolment {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private Boolean deleted;
    @ManyToOne
    private Student student;

}
