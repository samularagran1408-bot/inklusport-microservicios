package com.inklusport.admin.exception;

/**
 * Excepcion lanzada cuando un recurso no es encontrado.
 * Se utiliza para entidades que no existen en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
