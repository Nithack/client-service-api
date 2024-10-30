package com.nithack.clientService.domain.entity;


import com.nithack.clientService.infra.database.model.AddressModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientEntity {
    private UUID id;
    private String cpf;
    private String name;
    private LocalDate dateOfBirth;
    private String phone;
    private AddressModel address;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
