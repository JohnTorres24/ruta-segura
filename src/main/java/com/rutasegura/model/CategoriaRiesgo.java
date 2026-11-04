package com.rutasegura.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;

/**
 * Categorías de riesgo disponibles para el reporte rápido (One-Tap).
 * Soporta normalización flexible de texto (mayúsculas, minúsculas, espacios y tildes).
 */
public enum CategoriaRiesgo {
    ACOSO("Acoso"),
    ILUMINACION_DEFICIENTE("Iluminación deficiente"),
    ZONA_DESOLADA("Zona desolada"),
    SOSPECHOSO("Persona o vehículo sospechoso"),
    OTRO("Otro");

    private final String descripcion;

    CategoriaRiesgo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

    /**
     * Convierte de manera tolerante cualquier representación textual a su correspondiente enum.
     * Elimina tildes, convierte a mayúsculas y reemplaza espacios por guiones bajos.
     */
    @JsonCreator
    public static CategoriaRiesgo desdeTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return OTRO;
        }

        // 1. Quitar tildes y diacríticos
        String limpio = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase()
                .replaceAll("[\\s\\-]+", "_");

        // 2. Coincidencia exacta por nombre
        for (CategoriaRiesgo cat : values()) {
            if (cat.name().equalsIgnoreCase(limpio)) {
                return cat;
            }
        }

        // 3. Coincidencia por subcadenas comunes
        if (limpio.contains("ILUMINAC") || limpio.contains("LUZ") || limpio.contains("OSCUR")) {
            return ILUMINACION_DEFICIENTE;
        }
        if (limpio.contains("ACOS")) {
            return ACOSO;
        }
        if (limpio.contains("DESOLAD") || limpio.contains("SOLITARI")) {
            return ZONA_DESOLADA;
        }
        if (limpio.contains("SOSPECH")) {
            return SOSPECHOSO;
        }

        return OTRO;
    }
}
