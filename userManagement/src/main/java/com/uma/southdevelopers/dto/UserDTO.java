package com.uma.southdevelopers.dto;

import com.uma.southdevelopers.entities.User;
import com.uma.southdevelopers.security.PasswordUtils;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
public class UserDTO {
    private long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;

    public static UserDTO fromUser(User user){
        return UserDTO.builder()
                .id(user.getUserId())
                .nombre(user.getName())
                .apellido1(user.getSurname1())
                .apellido2(user.getSurname2())
                .email(user.getEmail())
                .build();
    }

    public User user(){
        return User.builder()
                .userId(id)
                .name(nombre)
                .surname1(apellido1)
                .surname2(apellido2)
                .email(email)
                .build();
    }
}
