package com.inklusport.auth.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {

  public EmailAlreadyRegisteredException(String email) {
    super("El correo ya está registrado: " + email);
  }
}
