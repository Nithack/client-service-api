package com.nithack.clientService.infra.database.repository;


import com.nithack.clientService.infra.database.model.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, UUID> {
    Optional<ClientModel> findByCpf(String cpf);
    Boolean existsByCpf(String cpf);
}
