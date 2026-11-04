package com.rutasegura.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertidor JPA para persistir y leer de forma segura las categorías de riesgo en PostgreSQL.
 * Transforma valores con tildes, minúsculas o espacios provenientes de la base de datos al Enum correspondiente.
 */
@Converter(autoApply = true)
public class CategoriaRiesgoConverter implements AttributeConverter<CategoriaRiesgo, String> {

    @Override
    public String convertToDatabaseColumn(CategoriaRiesgo attribute) {
        if (attribute == null) {
            return CategoriaRiesgo.OTRO.name();
        }
        return attribute.name();
    }

    @Override
    public CategoriaRiesgo convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return CategoriaRiesgo.OTRO;
        }
        return CategoriaRiesgo.desdeTexto(dbData);
    }
}
