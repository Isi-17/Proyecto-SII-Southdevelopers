package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Subject {

    @Id
    private String name;
    @ManyToMany (mappedBy = "subjects")
    private List<Student> students;

}
