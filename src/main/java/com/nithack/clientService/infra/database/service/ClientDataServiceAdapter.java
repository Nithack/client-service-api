package com.nithack.clientService.infra.database.service;

import com.nithack.clientService.application.mapper.ClientMapper;
import com.nithack.clientService.application.port.ClientDataServicePort;
import com.nithack.clientService.domain.entity.ClientEntity;
import com.nithack.clientService.infra.database.model.ClientModel;
import com.nithack.clientService.infra.database.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter para operações de persistência de dados de clientes,
 * implementando lógica de mapeamento e acesso ao banco de dados.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientDataServiceAdapter implements ClientDataServicePort {

    private final ClientRepository clientRepository;

    /**
     * Exclui um cliente pelo ID.
     *
     * @param clientId UUID do cliente.
     */
    @Override
    public void deleteById(UUID clientId) {
        log.info("[deleteById] Deleting client with id: {}", clientId);
        try {
            clientRepository.deleteById(clientId);
            log.info("[deleteById] Successfully deleted client with id: {}", clientId);
        } catch (Exception e) {
            log.error("[deleteById] Error deleting client with id: {}", clientId, e);
            throw e;
        } finally {
            log.info("[deleteById] Finished deleting client with id: {}", clientId);
        }
    }

    /**
     * Retorna todos os clientes cadastrados, convertendo-os para ClientEntity.
     *
     * @return lista de ClientEntity.
     */
    @Override
    public List<ClientEntity> findAll() {
        log.info("[findAll] Retrieving all clients from database");
        try {
            List<ClientModel> clientModels = clientRepository.findAll();
            if (clientModels.isEmpty()) {
                log.warn("[findAll] No clients found");
                return List.of();
            }
            log.info("[findAll] Successfully found {} clients, start mapping", clientModels.size());
            List<ClientEntity> clientEntities = clientModels.stream()
                    .map(ClientMapper::toEntity)
                    .collect(Collectors.toList());
            log.info("[findAll] Successfully mapped {} clients", clientEntities.size());
            return clientEntities;
        } catch (Exception e) {
            log.error("[findAll] Error retrieving all clients from database", e);
            throw e;
        } finally {
            log.info("[findAll] Finished retrieving all clients from database");
        }
    }

    /**
     * Busca um cliente pelo ID, retornando-o como ClientEntity.
     *
     * @param clientId UUID do cliente.
     * @return Optional com ClientEntity, ou vazio se não encontrado.
     */
    @Override
    public Optional<ClientEntity> findById(UUID clientId) {
        log.info("[findById] Retrieving client with id: {}", clientId);
        try {
            Optional<ClientModel> clientModel = clientRepository.findById(clientId);
            log.info("[findById] Start mapping client with id: {}", clientId);
            Optional<ClientEntity> clientEntity = clientModel.map(ClientMapper::toEntity);
            log.info("[findById] Successfully mapped client with id: {}", clientId);
            return clientEntity;
        } catch (Exception e) {
            log.error("[findById] Error retrieving client with id: {}", clientId, e);
            throw e;
        } finally {
            log.info("[findById] Finished retrieving client with id: {}", clientId);
        }
    }

    /**
     * Salva ou atualiza um cliente, mapeando de ClientEntity para ClientModel.
     *
     * @param clientEntity o cliente a ser salvo.
     * @return ClientEntity salvo.
     */
    @Override
    public ClientEntity save(ClientEntity clientEntity) {
        log.info("[save] Saving client with id: {}", clientEntity.getId());
        try {
            ClientModel clientModel = ClientMapper.toModel(clientEntity);
            ClientModel savedClient = clientRepository.save(clientModel);
            log.info("[save] Successfully saved client with id: {}", savedClient.getId());
            log.info("[save] Start mapping saved client with id: {}", savedClient.getId());
            ClientEntity savedClientEntity = ClientMapper.toEntity(savedClient);
            log.info("[save] Successfully mapped saved client with id: {}", savedClient.getId());
            return savedClientEntity;
        } catch (Exception e) {
            log.error("[save] Error saving client with id: {}", clientEntity.getId());
            throw e;
        } finally {
            log.info("[save] Finished saving client with id: {}", clientEntity.getId());
        }
    }

    /**
     * Verifica se um cliente existe pelo ID.
     *
     * @param clientId UUID do cliente.
     * @return true se o cliente existir, false caso contrário.
     */
    @Override
    public boolean existsById(UUID clientId) {
        log.info("[existsById] Checking if client exists with id: {}", clientId);
        try {
            boolean exists = clientRepository.existsById(clientId);
            log.info("[existsById] Client with id: {} exists: {}", clientId, exists);
            return exists;
        } catch (Exception e) {
            log.error("[existsById] Error checking existence of client with id: {}", clientId);
            throw e;
        }
    }

    /**
     * Verifica se um cliente existe pelo CPF.
     *
     * @param cpf CPF do cliente.
     * @return true se o cliente existir, false caso contrário.
     */
    @Override
    public boolean existsByCpf(String cpf) {
        log.info("[existsByCpf] Checking if client exists CPF");
        try {
            boolean exists = clientRepository.existsByCpf(cpf);
            log.info("[existsByCpf] Client with CPF exists: {}", exists);
            return exists;
        } catch (Exception e) {
            log.error("[existsByCpf] Error checking existence of client CPF");
            throw e;
        }
    }
}
