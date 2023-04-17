package com.uma.southdevelopers.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Institute {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private Integer zipcode;
    private String country;
}
