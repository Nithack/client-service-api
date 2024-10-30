package com.nithack.clientService.infra.http.controller;

import com.nithack.clientService.application.dto.ErrorResponseDTO;
import com.nithack.clientService.application.exception.ClientAlreadyExistsException;
import com.nithack.clientService.application.exception.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Tratamento de exceção para ClientAlreadyExistsException.
     *
     * @param ex A exceção ClientAlreadyExistsException.
     * @return Resposta com mensagem de erro e status HTTP CONFLICT (409).
     */
    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleClientAlreadyExistsException(ClientAlreadyExistsException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Tratamento de exceção para ClientNotFoundException.
     *
     * @param ex A exceção ClientNotFoundException.
     * @return Resposta com mensagem de erro e status HTTP NOT FOUND (404).
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleClientNotFoundException(ClientNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Tratamento para exceções genéricas não mapeadas.
     *
     * @param ex A exceção não mapeada.
     * @param request Detalhes da solicitação que gerou o erro.
     * @return Resposta com mensagem genérica de erro e status HTTP INTERNAL SERVER ERROR (500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
