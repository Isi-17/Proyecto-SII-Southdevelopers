package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class SpecialNeeds {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "specialNeeds")
    private List<Student> students;
}
