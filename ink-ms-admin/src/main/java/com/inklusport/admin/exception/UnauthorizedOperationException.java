package com.inklusport.admin.exception;

/**
 * Excepcion lanzada cuando un usuario no tiene permisos para realizar una operacion.
 */
public class UnauthorizedOperationException extends RuntimeException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }

    public UnauthorizedOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
