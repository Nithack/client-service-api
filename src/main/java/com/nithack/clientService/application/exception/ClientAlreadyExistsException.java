package com.nithack.clientService.application.exception;


public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String cpf) {
        super("Client already exists with CPF: " + cpf);
    }
}
