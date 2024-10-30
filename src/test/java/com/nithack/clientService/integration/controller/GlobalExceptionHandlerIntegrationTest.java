package com.nithack.clientService.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nithack.clientService.TestcontainersConfiguration;
import com.nithack.clientService.application.dto.ClientDTO;
import com.nithack.clientService.application.exception.ClientAlreadyExistsException;
import com.nithack.clientService.application.exception.ClientNotFoundException;
import com.nithack.clientService.infra.database.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @MockBean
    private ClientRepository mockClientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 409 Conflict when ClientAlreadyExistsException is thrown")
    void shouldReturnConflictWhenClientAlreadyExists() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        doThrow(new ClientAlreadyExistsException(clientDTO.getCpf())).when(mockClientRepository).save(any());

        mockMvc.perform(post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.name()))
                .andExpect(jsonPath("$.message").value("Client already exists with CPF: " + clientDTO.getCpf()));
    }

    @Test
    @DisplayName("Should return 404 Not Found when ClientNotFoundException is thrown")
    void shouldReturnNotFoundWhenClientNotFound() throws Exception {
        UUID clientId = UUID.randomUUID();

        // Simula a exceção ClientNotFoundException ao tentar buscar um cliente inexistente
        doThrow(new ClientNotFoundException(clientId)).when(mockClientRepository).findById(clientId);

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("Client not found with id: " + clientId));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for general exceptions")
    void shouldReturnInternalServerErrorForGeneralException() throws Exception {
        UUID clientId = UUID.randomUUID();

        // Simula uma exceção genérica ao buscar um cliente
        doThrow(new RuntimeException("Unexpected error")).when(mockClientRepository).findById(clientId);

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred. Please try again later."));
    }

    private ClientDTO createClientDTO() {
        return ClientDTO.builder()
                .name("Test Name")
                .cpf("12345678901")
                .phone("1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Campinas")
                .state("SP")
                .street("Rua Exemplo")
                .postalCode("13020000")
                .build();
    }
}
