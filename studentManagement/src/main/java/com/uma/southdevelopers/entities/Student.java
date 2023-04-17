package com.uma.southdevelopers.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Student {
    @Id @GeneratedValue
    private Long Id;
    @OneToOne
    private CompleteName name;
    private String DNI;
    private String phone;
    private String email;
    @OneToMany
    private List<Enrolment> subjects;
    private Long idCampus;
    @OneToOne
    private Institute institute;
    private boolean noDelete;

}
