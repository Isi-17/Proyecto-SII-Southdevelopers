package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Student {

    @Id
    private String DNI;
    private String name;
    private String surnames;
    @ManyToMany
    private List<Subject> subjects;


}
