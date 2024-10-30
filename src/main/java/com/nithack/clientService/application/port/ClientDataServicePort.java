package com.nithack.clientService.application.port;

import com.nithack.clientService.domain.entity.ClientEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientDataServicePort {
    /**
     * Exclui um cliente pelo seu ID.
     *
     * @param clientId UUID do cliente a ser excluído.
     */
    void deleteById(UUID clientId);
    List<ClientEntity> findAll();
    /**
     * Busca um cliente pelo seu ID.
     *
     * @param clientId UUID do cliente a ser buscado.
     * @return Optional com o ClientEntity encontrado, ou Optional vazio se não encontrado.
     */
    Optional<ClientEntity> findById(UUID clientId);
    /**
     * Salva ou atualiza um cliente.
     *
     * @param client a entidade de cliente a ser salvo.
     * @return o ClientModel salvo.
     */
    ClientEntity save(ClientEntity client);
    /**
     * Verifica se um cliente existe pelo seu ID.
     *
     * @param clientId UUID do cliente.
     * @return true se o cliente existir, caso contrário false.
     */
    boolean existsById(UUID clientId);
    /**
     * Verifica se um cliente já existe pelo CPF.
     *
     * @param cpf CPF do cliente.
     * @return true se o cliente existir, caso contrário false.
     */
    boolean existsByCpf(String cpf);
}
