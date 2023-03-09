package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Student {

    @Id
    private String DNI;
    private String name;
    private String surnames;
    @ElementCollection(targetClass = Subject.class)
    @Enumerated(EnumType.STRING)
    private List<Subject> subjects;
    private String email;
    private Integer phoneNumber;
    private String specialNeeds;
    private String school;


}
