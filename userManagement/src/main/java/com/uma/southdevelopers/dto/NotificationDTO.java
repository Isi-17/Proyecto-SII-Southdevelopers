package com.uma.southdevelopers.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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
