package com.uma.southdevelopers.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialNeeds {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "specialNeeds")
    private List<Student> students;
}
