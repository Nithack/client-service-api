package com.nithack.clientService.infra.http.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private int port;

    @Bean
    public OpenAPI clientServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Client Service API")
                        .description("API para gerenciamento de clientes, incluindo operações de criação, leitura, atualização e exclusão.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Suporte Nithack")
                                .email("support@nithack.com")
                                .url("https://nithack.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:" + port).description("Ambiente Local"),
                        new Server().url("https://api.nithack.com").description("Ambiente de Produção")
                ))
                .tags(List.of(
                        new Tag().name("Client Management").description("Operações para gerenciamento de clientes")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Completa")
                        .url("https://nithack.com/docs"));
    }
}