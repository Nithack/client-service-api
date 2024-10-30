package com.nithack.clientService.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Classe para padronizar a resposta de erro com mensagem e status HTTP.
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorResponseDTO {
    private HttpStatus status;
    private String message;
}
