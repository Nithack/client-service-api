package com.nithack.clientService.application.mapper;


import com.nithack.clientService.application.dto.ClientDTO;
import com.nithack.clientService.domain.entity.AddressEntity;
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
        var addressModel = AddressModel.builder()
                .id(entity.getAddress().getId())
                .city(entity.getAddress().getCity())
                .state(entity.getAddress().getState())
                .street(entity.getAddress().getStreet())
                .postalCode(entity.getAddress().getPostalCode())
                .build();

        return ClientModel.builder()
                .id(entity.getId())
                .cpf(entity.getCpf())
                .name(entity.getName())
                .dateOfBirth(entity.getDateOfBirth())
                .phone(entity.getPhone())
                .address(addressModel)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static ClientEntity toEntity(ClientModel model) {
        if (model == null) {
            return null;
        }
        var addressEntity = AddressEntity.builder()
                .id(model.getAddress().getId())
                .city(model.getAddress().getCity())
                .state(model.getAddress().getState())
                .street(model.getAddress().getStreet())
                .postalCode(model.getAddress().getPostalCode())
                .build();
        return ClientEntity.builder()
                .id(model.getId())
                .cpf(model.getCpf())
                .name(model.getName())
                .dateOfBirth(model.getDateOfBirth())
                .phone(model.getPhone())
                .address(addressEntity)
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public static ClientEntity toEntity(ClientDTO dto) {
        var addressEntity = AddressEntity.builder()
                .city(dto.getCity())
                .state(dto.getState())
                .street(dto.getStreet())
                .postalCode(dto.getPostalCode())
                .build();

        return ClientEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .cpf(dto.getCpf())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .address(addressEntity)
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