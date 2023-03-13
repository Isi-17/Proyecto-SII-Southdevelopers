package com.uma.southdevelopers;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class Student {

    @Id
    private String DNI;
    private String name;
    private String surnames;
    private String email;
    private String phoneNumber;
    @ManyToMany
    private List<SpecialNeeds> specialNeeds;
    private String school;
    @OneToOne(fetch = FetchType.LAZY)
    private Enrolment enrolment;


}
