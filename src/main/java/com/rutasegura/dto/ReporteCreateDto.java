package com.rutasegura.dto;

import com.rutasegura.model.CategoriaRiesgo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de transferencia de datos para la creación de un reporte rápido (One-Tap).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteCreateDto {

    @NotNull(message = "La categoría de riesgo es obligatoria.")
    private CategoriaRiesgo categoria;

    @NotBlank(message = "El municipio es obligatorio.")
    @Size(max = 100, message = "El municipio no puede superar los 100 caracteres.")
    private String municipio;

    @NotBlank(message = "El punto de referencia es obligatorio.")
    @Size(max = 255, message = "El punto de referencia no puede superar los 255 caracteres.")
    private String referencia;

    private Double latitud;

    private Double longitud;
}
