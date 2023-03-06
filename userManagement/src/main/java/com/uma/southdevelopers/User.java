package com.uma.southdevelopers;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id @GeneratedValue
    private Long userId;
    private String email;
    private String password;

}
