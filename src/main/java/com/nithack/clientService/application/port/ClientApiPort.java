package com.nithack.clientService.application.port;

import com.nithack.clientService.application.dto.ClientDTO;
import com.nithack.clientService.application.dto.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

public interface ClientApiPort {

    @Operation(summary = "Create a new client", description = "Creates a new client with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid client data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ClientDTO> createClient(@Parameter(description = "Client details", required = true)
                                           @Valid @RequestBody ClientDTO clientDTO);

    @Operation(summary = "Get client by ID", description = "Fetches a client by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ClientDTO> getClientById(@Parameter(description = "ID of the client", required = true)
                                            @PathVariable UUID id);

    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of clients retrieved",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class)))
    })
    ResponseEntity<List<ClientDTO>> getAllClients();

    @Operation(summary = "Update client by ID", description = "Updates an existing client by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid client data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ClientDTO> updateClient(@Parameter(description = "ID of the client", required = true)
                                           @PathVariable UUID id,
                                           @Parameter(description = "Updated client details", required = true)
                                           @Valid @RequestBody ClientDTO clientDTO);

    @Operation(summary = "Delete client by ID", description = "Deletes an existing client by the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<Void> deleteClient(@Parameter(description = "ID of the client", required = true)
                                      @PathVariable UUID id);
}