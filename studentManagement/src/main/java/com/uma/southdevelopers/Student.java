package com.uma.southdevelopers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    private String DNI;

    @NonNull
    private String name;

    private String surnames;
    
    @OneToMany
    private List<Enrolment> enrolment;

    private String email;

    private String phoneNumber;

    @ManyToMany
    private List<SpecialNeeds> specialNeeds;

    private String examsId;
    private String school;

}
