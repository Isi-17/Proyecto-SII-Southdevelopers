package com.uma.southdevelopers.entities;

import lombok.Data;

import jakarta.persistence.*;
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