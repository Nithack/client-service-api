package com.nithack.clientService.application.service;


import com.nithack.clientService.application.exception.ClientAlreadyExistsException;
import com.nithack.clientService.application.exception.ClientNotFoundException;
import com.nithack.clientService.application.mapper.ClientMapper;
import com.nithack.clientService.application.port.ClientDataServicePort;
import com.nithack.clientService.application.services.ClientDataServiceAdapter;
import com.nithack.clientService.application.services.ClientServiceAdapter;
import com.nithack.clientService.domain.entity.AddressEntity;
import com.nithack.clientService.domain.entity.ClientEntity;
import com.nithack.clientService.infra.database.model.AddressModel;
import com.nithack.clientService.infra.database.model.ClientModel;
import com.nithack.clientService.infra.database.repository.ClientRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    private ClientServiceAdapter clientService;

    @BeforeEach
    void setUp() {
        ClientDataServicePort clientDataService = new ClientDataServiceAdapter(clientRepository);
        clientService = new ClientServiceAdapter(clientDataService);
    }

    @Test
    @DisplayName("Should create a client with valid data")
    void createClient_ShouldReturnClient_WhenDataIsValid() {
        final UUID idTest = UUID.randomUUID();
        ClientEntity clientEntity = getClientEntity(idTest);
        ClientModel clientModel = getClientModel(idTest, clientEntity.getName(), clientEntity.getCpf());

        when(clientRepository.save(any(ClientModel.class))).thenReturn(clientModel);

        ClientEntity createdClient = clientService.create(clientEntity);
        assertThat(createdClient).isNotNull();
        assertThat(createdClient.getName()).isEqualTo(clientEntity.getName());
        verify(clientRepository, times(1)).save(any(ClientModel.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create client with existing CPF")
    void createClient_ShouldThrowException_WhenCpfAlreadyExists() {
        ClientEntity clientEntity = getClientEntity(UUID.randomUUID());
        when(clientRepository.existsByCpf(clientEntity.getCpf())).thenReturn(true);

        assertThrows(ClientAlreadyExistsException.class, () -> clientService.create(clientEntity));
        verify(clientRepository, never()).save(any(ClientModel.class));
    }

    @Test
    @DisplayName("Should update existing client and return updated data")
    void updateClient_ShouldUpdateAndReturnUpdatedClient_WhenClientExists() {
        UUID clientId = UUID.randomUUID();
        ClientEntity clientEntity = getClientEntity(clientId);
        ClientModel clientModelSaved = ClientMapper.toModel(clientEntity);

        ClientModel clientModel = getClientModel(clientId, "Updated Name", clientModelSaved.getCpf());

        when(clientRepository.findById(clientId)).thenReturn(Optional.ofNullable(clientModelSaved));
        when(clientRepository.save(any(ClientModel.class))).thenReturn(clientModel);

        ClientEntity updatedClient = clientService.update(clientEntity);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo("Updated Name");
        verify(clientRepository, times(1)).save(any(ClientModel.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to update non-existing client")
    void updateClient_ShouldThrowException_WhenClientNotFound() {
        UUID clientId = UUID.randomUUID();
        ClientEntity clientEntity = getClientEntity(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.update(clientEntity));
        verify(clientRepository, never()).save(any(ClientModel.class));
    }

    @Test
    @DisplayName("Should delete existing client")
    void deleteClient_ShouldDeleteClient_WhenClientExists() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.existsById(clientId)).thenReturn(true);

        clientService.delete(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete non-existing client")
    void deleteClient_ShouldThrowException_WhenClientDoesNotExist() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.existsById(clientId)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> clientService.delete(clientId));
        verify(clientRepository, never()).deleteById(clientId);
    }
    @Test
    @DisplayName("Should return all clients")
    void findAll_ShouldReturnListOfClients_WhenClientsExist() {
        UUID clientId1 = UUID.randomUUID();
        UUID clientId2 = UUID.randomUUID();
        ClientModel clientModel1 = getClientModel(clientId1, "Test Name 1", "11111111111");
        ClientModel clientModel2 = getClientModel(clientId2, "Test Name 2", "22222222222");

        when(clientRepository.findAll()).thenReturn(List.of(clientModel1, clientModel2));

        List<ClientEntity> clients = clientService.findAll();
        assertThat(clients).hasSize(2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no clients exist")
    void findAll_ShouldReturnEmptyList_WhenNoClientsExist() {
        when(clientRepository.findAll()).thenReturn(List.of());

        List<ClientEntity> clients = clientService.findAll();
        assertThat(clients).isEmpty();
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return client by ID")
    void findById_ShouldReturnClient_WhenClientExists() {
        UUID clientId = UUID.randomUUID();
        ClientModel clientModel = getClientModel(clientId, "Test Name", "12341234512");
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(clientModel));

        Optional<ClientEntity> client = clientService.findById(clientId.toString());
        assertThat(client).isPresent();
        assertThat(client.get().getId()).isEqualTo(clientId);
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    @DisplayName("Should throw ClientNotFoundException when client does not exist")
    void findById_ShouldThrowClientNotFoundException_WhenClientDoesNotExist() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        Optional<ClientEntity> client = clientService.findById(clientId.toString());
        assertThat(client).isEmpty();
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    @DisplayName("Should throw exception when error occurs during findAll")
    void findAll_ShouldThrowException_WhenErrorOccurs() {
        when(clientRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> clientService.findAll());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when error occurs during findById")
    void findById_ShouldThrowException_WhenErrorOccurs() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.findById(clientId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> clientService.findById(clientId.toString()));
        verify(clientRepository, times(1)).findById(clientId);
    }


    private static ClientEntity getClientEntity(UUID id) {
        return ClientEntity.builder()
                .id(id)
                .name("Test Name")
                .cpf("12341234512")
                .phone("112313321")
                .dateOfBirth(LocalDate.now())
                .address(AddressEntity.builder()
                        .id(UUID.randomUUID())
                        .city("Campinas")
                        .state("SP")
                        .street("Rua 13 de Abril")
                        .postalCode("13020020")
                        .build())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
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
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }
}

