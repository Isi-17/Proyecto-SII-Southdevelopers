package com.uma.southdevelopers;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Enrolment {
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToOne(fetch = FetchType.LAZY)
    private Student student;
}
