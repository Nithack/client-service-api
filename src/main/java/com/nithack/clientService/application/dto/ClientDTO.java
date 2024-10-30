package com.nithack.clientService.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) para operações de cliente, contendo validações.
 */
@Data
@Builder
public class ClientDTO {

    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name can have a maximum of 50 characters")
    private String name;

    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}", message = "CPF must have exactly 11 digits")
    private String cpf;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Postal code is required")
    private String postalCode;
}