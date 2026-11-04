package com.rutasegura.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura estándar de respuesta para errores de la API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

    private LocalDateTime marcaTiempo;
    private int codigoEstado;
    private String error;
    private String mensaje;
    private String ruta;
    private Map<String, String> detallesValidacion;
}
