package com.uma.southdevelopers.dtos;


import lombok.*;

import java.net.URI;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Links {
    private URI self;
}
