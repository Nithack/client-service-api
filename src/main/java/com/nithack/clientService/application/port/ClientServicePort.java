package com.nithack.clientService.application.port;

import com.nithack.clientService.application.exception.ClientAlreadyExistsException;
import com.nithack.clientService.application.exception.ClientNotFoundException;
import com.nithack.clientService.domain.entity.ClientEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Interface para operações de gerenciamento de clientes, como criação, atualização, consulta e exclusão.
 */
public interface ClientServicePort {

    /**
     * Exclui um cliente pelo ID.
     *
     * @param clientId UUID do cliente.
     * @throws ClientNotFoundException se o cliente não existir.
     */
    void delete(UUID clientId);

    /**
     * Retorna todos os clientes cadastrados.
     *
     * @return lista de clientes ou lista vazia, se nenhum cliente for encontrado.
     */
    List<ClientEntity> findAll();

    /**
     * Busca um cliente pelo ID.
     *
     * @param id UUID do cliente como string.
     * @return Optional com o cliente, ou vazio se não encontrado.
     */
    Optional<ClientEntity> findById(String id);

    /**
     * Atualiza um cliente existente.
     *
     * @param client dados atualizados do cliente.
     * @return ClientEntity com o cliente atualizado.
     * @throws ClientNotFoundException se o cliente não existir.
     */
    ClientEntity update(ClientEntity client);

    /**
     * Cria um novo cliente.
     *
     * @param clientDto dados do novo cliente.
     * @return ClientEntity com o cliente criado.
     * @throws ClientAlreadyExistsException se um cliente com o mesmo CPF já existir.
     */
    ClientEntity create(ClientEntity clientDto);
}

