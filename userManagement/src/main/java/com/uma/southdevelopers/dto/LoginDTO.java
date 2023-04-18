package com.uma.southdevelopers.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginDTO {
    private String email;
    private String password;
}
