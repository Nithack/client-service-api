package com.nithack.clientService.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nithack.clientService.TestcontainersConfiguration;
import com.nithack.clientService.application.dto.ClientDTO;
import com.nithack.clientService.infra.database.model.AddressModel;
import com.nithack.clientService.infra.database.model.ClientModel;
import com.nithack.clientService.infra.database.repository.ClientRepository;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a new client")
    void shouldCreateNewClient() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        mockMvc.perform(post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(clientDTO.getName()))
                .andExpect(jsonPath("$.cpf").value(clientDTO.getCpf()));
    }

    @Test
    @DisplayName("Should return client by ID")
    void shouldReturnClientById() throws Exception {
        UUID clientId = UUID.randomUUID();
        ClientModel clientModel = getClientModel(clientId, "Test Name", "31234678901");
        clientRepository.save(clientModel);

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId.toString()))
                .andExpect(jsonPath("$.name").value(clientModel.getName()));
    }

    @Test
    @DisplayName("Should return 404 when client is not found")
    void shouldReturn404WhenClientNotFound() throws Exception {
        mockMvc.perform(get("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update existing client")
    void shouldUpdateClient() throws Exception {
        UUID clientId = UUID.randomUUID();
        ClientModel clientModel = getClientModel(clientId, "Should Update", "12345678901");

        clientRepository.save(clientModel);

        ClientDTO updatedClientDTO = createClientDTO();
        updatedClientDTO.setName("Updated Name");

        mockMvc.perform(put("/clients/{id}", clientId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedClientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @DisplayName("Should delete client by ID")
    void shouldDeleteClientById() throws Exception {
        UUID clientId = UUID.randomUUID();
        ClientModel clientModel = getClientModel(clientId, "Test Name", "12345678301");

        clientRepository.save(clientModel);

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return all clients")
    void shouldReturnAllClients() throws Exception {
        ClientModel client1 = getClientModel(UUID.randomUUID(), "Test Name1", "12345678901");
        ClientModel client2 = getClientModel(UUID.randomUUID(), "Test Name2", "45131235613");

        clientRepository.saveAll(List.of(client1, client2));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    private static ClientModel getClientModel(UUID id, String name, String cpf) {
        return ClientModel.builder()
                .id(id)
                .name(name)
                .cpf(cpf)
                .address(AddressModel.builder()
                        .id(UUID.randomUUID())
                        .city("Campinas")
                        .state("SP")
                        .street("Rua 13 de Abril")
                        .postalCode("13020020")
                        .build())
                .dateOfBirth(LocalDate.now())
                .phone(getRandomPhoneNumber())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    private static String getRandomPhoneNumber() {
        int min = 100000000;
        int max = 999999999;
        int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return String.format("%d", random);
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
            .postalCode("13020000").build();
    }
}
