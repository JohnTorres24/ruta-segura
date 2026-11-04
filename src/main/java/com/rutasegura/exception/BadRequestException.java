package com.rutasegura.exception;

/**
 * Excepción lanzada cuando los datos de una solicitud son inválidos o entran en conflicto.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
