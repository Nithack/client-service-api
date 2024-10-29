package com.nithack.clientService.application.port;

import java.util.Optional;
import java.util.UUID;

public interface ClientServicePort {
    void delete(UUID clientId);
    List<ClientEntity> findAll();
    void update(UUID clientId, ClientEntity client);
    Optional<ClientEntity> create(ClientEntity clientDto);
    Optional<ClientToken> authenticate(String cpf, String password);
}
