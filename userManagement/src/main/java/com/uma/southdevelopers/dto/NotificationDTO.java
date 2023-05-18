package com.uma.southdevelopers.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String asunto;
    private String cuerpo;
    private String emailDestino;
    private String telefonoDestino;
    private String programacionEnvio;
    private List<String> medios;
    private String tipoDeNotificacion;
}
