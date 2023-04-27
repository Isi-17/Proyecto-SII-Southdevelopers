package com.uma.southdevelopers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
