package com.rutasegura;

import com.rutasegura.model.CategoriaRiesgo;
import com.rutasegura.model.CategoriaRiesgoConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoriaRiesgoTest {

    private final CategoriaRiesgoConverter converter = new CategoriaRiesgoConverter();

    @ParameterizedTest
    @ValueSource(strings = {
            "ILUMINACION_DEFICIENTE",
            "Iluminación deficiente",
            "iluminacion deficiente",
            "Iluminacion Deficiente",
            "iluminación_deficiente"
    })
    @DisplayName("Debe normalizar variantes textuales a ILUMINACION_DEFICIENTE")
    void debeNormalizarIluminacionDeficiente(String input) {
        CategoriaRiesgo resultado = CategoriaRiesgo.desdeTexto(input);
        assertEquals(CategoriaRiesgo.ILUMINACION_DEFICIENTE, resultado);
        assertEquals(CategoriaRiesgo.ILUMINACION_DEFICIENTE, converter.convertToEntityAttribute(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACOSO", "Acoso", "acoso"})
    @DisplayName("Debe normalizar variantes a ACOSO")
    void debeNormalizarAcoso(String input) {
        assertEquals(CategoriaRiesgo.ACOSO, CategoriaRiesgo.desdeTexto(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ZONA_DESOLADA", "Zona desolada", "zona_desolada", "Zona Desolada"})
    @DisplayName("Debe normalizar variantes a ZONA_DESOLADA")
    void debeNormalizarZonaDesolada(String input) {
        assertEquals(CategoriaRiesgo.ZONA_DESOLADA, CategoriaRiesgo.desdeTexto(input));
    }

    @Test
    @DisplayName("Debe asignar OTRO a textos no reconocidos o valores nulos")
    void debeAsignarOtroParaValoresDesconocidos() {
        assertEquals(CategoriaRiesgo.OTRO, CategoriaRiesgo.desdeTexto("desconocido"));
        assertEquals(CategoriaRiesgo.OTRO, CategoriaRiesgo.desdeTexto(null));
        assertEquals(CategoriaRiesgo.OTRO, converter.convertToEntityAttribute(null));
    }
}
