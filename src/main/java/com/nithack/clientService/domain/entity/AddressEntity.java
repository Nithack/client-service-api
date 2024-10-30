package com.nithack.clientService.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
}
