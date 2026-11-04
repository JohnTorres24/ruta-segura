package com.rutasegura.exception;

/**
 * Excepción lanzada cuando ocurre un conflicto con el estado actual del recurso (ej. correo duplicado).
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String mensaje) {
        super(mensaje);
    }
}
