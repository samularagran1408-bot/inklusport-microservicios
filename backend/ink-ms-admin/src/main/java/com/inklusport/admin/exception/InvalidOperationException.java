package com.inklusport.admin.exception;

/**
 * Excepcion lanzada cuando se intenta realizar una operacion invalida.
 * Ejemplo: aprobar una solicitud que ya fue aprobada.
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
