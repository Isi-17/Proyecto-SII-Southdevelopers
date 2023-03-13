package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Enrolment {


    @Id @GeneratedValue
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToOne(fetch = FetchType.LAZY)
    private Student student;

}
