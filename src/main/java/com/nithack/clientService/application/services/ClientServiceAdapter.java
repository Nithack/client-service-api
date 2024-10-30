package com.nithack.clientService.application.services;


import com.nithack.clientService.application.exception.ClientAlreadyExistsException;
import com.nithack.clientService.application.exception.ClientNotFoundException;
import com.nithack.clientService.application.port.ClientDataServicePort;
import com.nithack.clientService.application.port.ClientServicePort;
import com.nithack.clientService.domain.entity.ClientEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceAdapter implements ClientServicePort {

    private final ClientDataServicePort clientDataService;

    @Override
    public void delete(UUID clientId) {
        log.info("[delete] Deleting client with id: {}", clientId);
        try {
            if (!clientDataService.existsById(clientId)) {
                log.warn("[delete] Client with id: {} does not exist", clientId);
                throw new ClientNotFoundException(clientId);
            }
            clientDataService.deleteById(clientId);
            log.info("[delete] Successfully deleted client with id: {}", clientId);
        } catch (Exception e) {
            log.error("[delete] Error deleting client with id: {}", clientId, e);
            throw e;
        } finally {
            log.info("[delete] Finished deleted client with id: {}", clientId);
        }
    }

    @Override
    public List<ClientEntity> findAll() {
       log.info("[findAll] Finding all clients");
       try {
           List<ClientEntity> clients = clientDataService.findAll();
           if (clients.isEmpty()) {
               log.warn("[findAll] No clients found");
               return List.of();
           }
           log.info("[findAll] Successfully found all clients");
           return clients;
       } catch (Exception e) {
           log.error("[findAll] Error finding all clients");
           throw e;
       } finally {
           log.info("[findAll] Finished finding all clients");
       }
    }

    @Override
    public Optional<ClientEntity> findById(String id) {
        log.info("[findById] Finding client with id: {}", id);
        try {
            Optional<ClientEntity> client = clientDataService.findById(UUID.fromString(id));
            if (client.isEmpty()) {
                log.warn("[findById] Client with id: {} does not exist", id);
                return Optional.empty();
            }
            log.info("[findById] Successfully found client with id: {}", id);
            return client;
        } catch (Exception e) {
            log.error("[findById] Error finding client with id: {}", id);
            throw e;
        } finally {
            log.info("[findById] Finished finding client with id: {}", id);
        }
    }

    @Override
    public ClientEntity update(ClientEntity client) {
        log.info("[update] Updating client with id: {}", client.getId());
        try {
            var optionalClient = clientDataService.findById(client.getId());
            if (optionalClient.isEmpty()) {
                log.warn("[update] Client with id: {} does not exist stop update", client.getId());
                throw new ClientNotFoundException(client.getId());
            }
            optionalClient.ifPresent(
                    c -> {
                        client.setUpdatedAt(LocalDate.now());
                        client.setCreatedAt(c.getCreatedAt());
                    }
            );
            final ClientEntity updateClientResult = clientDataService.save(client);
            log.info("[update] Successfully updated client with id: {}", client.getId());
            return updateClientResult;
        } catch (Exception e) {
            log.error("[update] Error updating client with id: {}", client.getId());
            throw e;
        } finally {
            log.info("[update] Finished updating client with id: {}", client.getId());
        }
    }

    @Override
    public ClientEntity create(ClientEntity client) {
        client.setId(UUID.randomUUID());
       log.info("[create] Start create new client with id: {}", client.getId());
       try {
           if (clientDataService.existsByCpf(client.getCpf())) {
               log.warn("[create] Client with cpf already exists");
               throw new ClientAlreadyExistsException(client.getCpf());
           }
           client.setCreatedAt(LocalDate.now());
           client.setUpdatedAt(LocalDate.now());
           final ClientEntity clientEntity = clientDataService.save(client);
           log.info("[create] Successfully created new client with id: {}", client.getId());
           return clientEntity;
       } catch (Exception e) {
           log.error("[create] Error creating new client with id: {}", client.getId());
           throw e;
       } finally {
           log.info("[create] Finished create new client with id: {}", client.getId());
       }
    }
}
