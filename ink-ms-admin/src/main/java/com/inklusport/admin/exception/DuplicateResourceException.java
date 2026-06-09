package com.inklusport.admin.exception;

/**
 * Excepcion lanzada cuando se intenta crear un recurso duplicado.
 * Ejemplo: un rol con nombre que ya existe.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
