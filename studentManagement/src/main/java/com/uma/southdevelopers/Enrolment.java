package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Enrolment {


    @Id @GeneratedValue
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToOne(fetch = FetchType.LAZY)
    private Student student;
    @ElementCollection(targetClass = Subject.class)
    @Enumerated(EnumType.STRING)
    private List<Subject> subjects;

}
