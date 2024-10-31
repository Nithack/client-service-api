# Cliente Service API

## Descrição

O **Cliente Service API** é um microserviço RESTful que gerencia os dados de clientes e oferece operações de **CRUD** (criação, leitura, atualização e exclusão) para o fluxo de seguros da aplicação. Este serviço desempenha um papel essencial na interação com o **Insurance Service API**, verificando se os clientes existem no sistema e fornecendo o CPF e outras informações relevantes durante as operações de simulação e contratação de seguros.

## Funcionalidade e Integração no Fluxo de Seguros

O Cliente Service API é utilizado pelo **Insurance Service API** para:

1. **Validação de Cliente**: Garante que o cliente exista antes de prosseguir com qualquer operação de seguro.
2. **Fornecimento de CPF**: Transmite o CPF do cliente, que é necessário para associar os seguros ao cliente correto no fluxo de simulação e contratação de seguro.
3. **Suporte a Operações de Seguro**: Permite que o Insurance Service consulte e valide informações do cliente, mantendo os dados consistentes em todo o sistema.

## Endpoints Principais

Para uma descrição detalhada dos endpoints disponíveis, consulte a [documentação interativa do Swagger](http://localhost:3030/swagger-ui/index.html), onde você encontrará informações sobre cada operação, incluindo os métodos HTTP e os parâmetros necessários.

## Exemplo de Integração com o Insurance Service

O Cliente Service API é consultado pelo **Insurance Service API** antes de qualquer operação de seguro para garantir a integridade dos dados. Aqui estão algumas maneiras de integração:

- **Simulação de Seguro**: O Insurance Service consulta o Cliente Service para verificar se o cliente existe e obter o CPF.
- **Contratação de Seguro**: Da mesma forma, o CPF e outras informações do cliente são validadas para assegurar que o seguro contratado corresponda ao cliente correto.

Caso o cliente não exista, o Insurance Service retorna uma mensagem de erro, interrompendo a operação de seguro.

## Configuração da Aplicação

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente para rodar o Cliente Service API:

```properties
# Configurações do Banco de Dados PostgreSQL
POSTGRES_DB_URL=jdbc:postgresql://localhost:5432/client_insurance
POSTGRES_DB_USERNAME=INTERNET
POSTGRES_DB_PASSWORD=q1w2e3r4

# Configurações da Aplicação
APP_PORT=3030
```

### Executando a Aplicação

Após configurar as variáveis de ambiente, você pode iniciar o serviço com:

```bash
./mvnw spring-boot:run
```

## Tecnologias Utilizadas

- **Spring Boot**: Framework principal para construção da API.
- **Spring Data JPA**: Persistência e mapeamento objeto-relacional.
- **Feign Client**: Integração com o Insurance Service API.
- **Swagger**: Documentação interativa dos endpoints.
