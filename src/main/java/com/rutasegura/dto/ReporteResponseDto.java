package com.rutasegura.dto;

import com.rutasegura.model.CategoriaRiesgo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos para la visualización de un reporte en el frontend.
 * Contiene propiedades duales (referencia/descripcion y categoria/tipo) para máxima compatibilidad con el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteResponseDto {

    private Long id;
    private CategoriaRiesgo categoria;
    private String tipo;
    private String municipio;
    private String referencia;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaReporte;
    private String nombreUsuaria;
    private Double distanciaKm;
}
