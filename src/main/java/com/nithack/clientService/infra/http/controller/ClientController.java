package com.nithack.clientService.infra.http.controller;

import com.nithack.clientService.application.services.ClientServiceAdapter;
import com.nithack.clientService.application.dto.ClientDTO;
import com.nithack.clientService.application.mapper.ClientMapper;
import com.nithack.clientService.domain.entity.ClientEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientServiceAdapter clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientEntity clientEntity = ClientMapper.toEntity(clientDTO);
        ClientEntity createdClient = clientService.create(clientEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDTO(createdClient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable UUID id) {
        Optional<ClientEntity> client = clientService.findById(id.toString());
        return client.map(value -> ResponseEntity.ok(ClientMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientEntity> clients = clientService.findAll();
        List<ClientDTO> clientDTOs = clients.stream().map(ClientMapper::toDTO).toList();
        return ResponseEntity.ok(clientDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientDTO clientDTO) {
        clientDTO.setId(id);
        ClientEntity clientEntity = ClientMapper.toEntity(clientDTO);
        ClientEntity updatedClient = clientService.update(clientEntity);
        return ResponseEntity.ok(ClientMapper.toDTO(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
