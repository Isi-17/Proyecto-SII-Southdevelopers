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
    @ElementCollection(targetClass = Subject.class)
    @Enumerated(EnumType.STRING)
    private List<Subject> subjects;
    private String email;
    private String phoneNumber;
    @ManyToMany
    private List<SpecialNeeds> specialNeeds;
    private String school;


}
