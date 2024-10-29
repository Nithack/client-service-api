package com.nithack.clientService.application.service;


import com.nithack.clientService.infra.repository.ClientRepository;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Optional;
import java.util.UUID;

@TestConfiguration
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar um cliente com dados válidos")
    void createClient_ShouldReturnClient_WhenDataIsValid() {
        ClientDto clientDto = new ClientDto("123.456.789-00", "John Doe", "1980-01-01", "(11) 91234-5678", "password123");
        Client client = new Client(UUID.randomUUID(), clientDto);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.createClient(clientDto);

        assertThat(createdClient).isNotNull();
        assertThat(createdClient.getName()).isEqualTo(clientDto.getName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar cliente com CPF já existente")
    void createClient_ShouldThrowException_WhenCpfAlreadyExists() {
        ClientDto clientDto = new ClientDto("123.456.789-00", "John Doe", "1980-01-01", "(11) 91234-5678", "password123");
        when(clientRepository.existsByCpf(clientDto.getCpf())).thenReturn(true);

        assertThrows(ClientAlreadyExistsException.class, () -> clientService.createClient(clientDto));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente existente e retornar dados atualizados")
    void updateClient_ShouldUpdateAndReturnUpdatedClient_WhenClientExists() {
        UUID clientId = UUID.randomUUID();
        ClientDto clientDto = new ClientDto("123.456.789-00", "Jane Doe", "1980-01-01", "(11) 91234-5678", "newpassword123");
        Client existingClient = new Client(clientId, new ClientDto("123.456.789-00", "John Doe", "1980-01-01", "(11) 91234-5678", "password123"));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client updatedClient = clientService.updateClient(clientId, clientDto);

        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo("Jane Doe");
        assertThat(updatedClient.getPassword()).isEqualTo("newpassword123");
        verify(clientRepository, times(1)).save(existingClient);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar cliente não encontrado")
    void updateClient_ShouldThrowException_WhenClientNotFound() {
        UUID clientId = UUID.randomUUID();
        ClientDto clientDto = new ClientDto("123.456.789-00", "Jane Doe", "1980-01-01", "(11) 91234-5678", "newpassword123");
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(clientId, clientDto));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Deve excluir cliente existente")
    void deleteClient_ShouldDeleteClient_WhenClientExists() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.existsById(clientId)).thenReturn(true);

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir cliente não encontrado")
    void deleteClient_ShouldThrowException_WhenClientDoesNotExist() {
        UUID clientId = UUID.randomUUID();
        when(clientRepository.existsById(clientId)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(clientId));
        verify(clientRepository, never()).deleteById(clientId);
    }

    @Test
    @DisplayName("Deve autenticar cliente com CPF e senha corretos")
    void authenticate_ShouldReturnClient_WhenCredentialsAreValid() {
        String cpf = "123.456.789-00";
        String password = "password123";
        Client client = new Client(UUID.randomUUID(), new ClientDto(cpf, "John Doe", "1980-01-01", "(11) 91234-5678", password));
        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(client));

        Client authenticatedClient = clientService.authenticate(cpf, password);

        assertThat(authenticatedClient).isNotNull();
        assertThat(authenticatedClient.getCpf()).isEqualTo(cpf);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar autenticar com senha incorreta")
    void authenticate_ShouldThrowException_WhenCredentialsAreInvalid() {
        String cpf = "123.456.789-00";
        String password = "wrongpassword";
        Client client = new Client(UUID.randomUUID(), new ClientDto(cpf, "John Doe", "1980-01-01", "(11) 91234-5678", "password123"));
        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(client));

        assertThrows(AuthenticationFailedException.class, () -> clientService.authenticate(cpf, password));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar autenticar cliente não encontrado")
    void authenticate_ShouldThrowException_WhenClientNotFound() {
        String cpf = "123.456.789-00";
        String password = "password123";
        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.authenticate(cpf, password));
    }
}

