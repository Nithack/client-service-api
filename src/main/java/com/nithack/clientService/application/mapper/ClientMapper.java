package com.nithack.clientService.application.mapper;


import com.nithack.clientService.application.dto.ClientDTO;
import com.nithack.clientService.domain.entity.ClientEntity;
import com.nithack.clientService.infra.database.model.AddressModel;
import com.nithack.clientService.infra.database.model.ClientModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMapper {

    private ClientMapper(){
        throw new RuntimeException("Util Class");
    }

    public static ClientModel toModel(ClientEntity entity) {
        if (entity == null) {
            return null;
        }
        return ClientModel.builder()
                .id(entity.getId())
                .cpf(entity.getCpf())
                .name(entity.getName())
                .dateOfBirth(entity.getDateOfBirth())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ClientEntity toEntity(ClientModel model) {
        if (model == null) {
            return null;
        }

        return ClientEntity.builder()
                .id(model.getId())
                .cpf(model.getCpf())
                .name(model.getName())
                .dateOfBirth(model.getDateOfBirth())
                .phone(model.getPhone())
                .address(model.getAddress())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public static ClientEntity toEntity(ClientDTO dto) {
        return ClientEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .cpf(dto.getCpf())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .address(AddressModel.builder()
                        .city(dto.getCity())
                        .state(dto.getState())
                        .street(dto.getStreet())
                        .postalCode(dto.getPostalCode())
                        .build())
                .build();
    }

    public static ClientDTO toDTO(ClientEntity entity) {
        var dto = ClientDTO.builder();
        dto.id(entity.getId());
        dto.name(entity.getName());
        dto.cpf(entity.getCpf());
        dto.phone(entity.getPhone());
        dto.dateOfBirth(entity.getDateOfBirth());
        dto.city(entity.getAddress().getCity());
        dto.state(entity.getAddress().getState());
        dto.street(entity.getAddress().getStreet());
        dto.postalCode(entity.getAddress().getPostalCode());
        return dto.build();
    }
}