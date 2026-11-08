# SupportFlow API

> API RESTful corporativa para gestão inteligente de chamados de suporte técnico, equipada com mensageria assíncrona (RabbitMQ) e triagem automatizada com inteligência artificial generativa (Google Gemini API).

---

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Principais Funcionalidades](#-principais-funcionalidades)
- [Arquitetura e Fluxo da Aplicação](#-arquitetura-e-fluxo-da-aplicação)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Regras de Negócio e Estados](#-regras-de-negócio-e-estados)
- [Endpoints da API](#-endpoints-da-api)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
  - [Executando com Docker Compose (Recomendado)](#executando-com-docker-compose-recomendado)
  - [Executando Localmente](#executando-localmente)
- [Testes Automatizados](#-testes-automatizados)
- [Documentação Interativa (Swagger/OpenAPI)](#-documentação-interativa-swaggeropenapi)

---

## 🎯 Visão Geral

O **SupportFlow** é uma solução de backend voltada para centralizar e otimizar o fluxo de atendimento a chamados de suporte técnico. Ao registrar um ticket, a aplicação dispara um evento assíncrono para uma fila de mensageria, onde um serviço consumidor encaminha a descrição do problema para a API do Google Gemini, persistindo uma análise e recomendação técnica automatizada para consulta da equipe e do solicitante.

A aplicação conta com autenticação stateless via tokens JWT, controle de acesso baseado em papéis (RBAC), validação rigorosa no ciclo de vida dos tickets e documentação interativa via OpenAPI / Swagger UI.

---

## 🚀 Principais Funcionalidades

- **Autenticação & Autorização:**
  - Cadastro de usuários com senhas criptografadas usando algoritmo BCrypt.
  - Autenticação stateless gerando tokens JWT (JSON Web Tokens).
  - Controle de perfis de acesso (`USER`, `ADMIN`, `SUPPORT`).
- **Gestão de Chamados (Tickets):**
  - Abertura de tickets com categorização (`ACCOUNT`, `BALANCE`, `PAYMENT`, `TRANSFER`, `TECHNICAL`, `OTHER`).
  - Definição padrão de status inicial (`OPEN`) e prioridade (`MEDIUM`).
  - Isolamento de dados: usuários comuns visualizam e consultam exclusivamente os seus próprios chamados.
  - Regras rígidas de transição de estado impedindo fechamento indevido ou reabertura de tickets finalizados.
- **Processamento Assíncrono com Mensageria:**
  - Publicação desacoplada de eventos de novos chamados no RabbitMQ (`DirectExchange`).
  - Consumo em segundo plano sem bloquear a resposta da requisição HTTP do usuário.
- **Triagem Automatizada por Inteligência Artificial:**
  - Integração direta com a API Google Gemini (via `HttpClient` nativo do Java).
  - Resiliência com mecanismo de retry automático para respostas com status HTTP 503 (*Service Unavailable*).
  - Armazenamento estruturado da resposta da IA vinculada ao ticket correspondente.
- **Tratamento Centralizado de Erros:**
  - Handler global (`@RestControllerAdvice`) padronizando mensagens e status HTTP para recursos não encontrados, transições de tickets inválidas e erros de validação.

---

## 🏗 Arquitetura e Fluxo da Aplicação

O fluxo principal de criação e análise de chamado funciona da seguinte forma:

```
[ Cliente HTTP ]
       │  POST /v1/ticket (Bearer JWT)
       ▼
[ TicketController ]
       │
       ▼
[ TicketService ] ──(1. Salva Ticket no BD)──► [ PostgreSQL ]
       │
       │  (2. Publica evento com TicketResponseDTO)
       ▼
[ RabbitMQ Exchange: supportflow.ticket.exchange ]
       │  Routing Key: supportflow.created
       ▼
[ RabbitMQ Queue: supportflow.ticket.created ]
       │
       ▼
[ TicketConsumer ]
       │
       ▼
[ AIResponseService ]
       │  (3. Envia prompt para análise)
       ▼
[ AIClient ] ────────► [ Google Gemini API ]
       │                          │
       │◄──(Resposta da Análise)──┘
       │
       ▼
[ AIResponseRepository ] ──(4. Salva Análise)──► [ PostgreSQL (Tabela ai_responses) ]
```

---

## 🛠 Tecnologias Utilizadas

| Componente | Tecnologia | Versão / Detalhes |
|---|---|---|
| **Linguagem** | Java | 21 (LTS) |
| **Framework Base** | Spring Boot | 4.1.1 |
| **Persistência de Dados** | Spring Data JPA / Hibernate | Mapeamento relacional e auditoria |
| **Segurança** | Spring Security | Autenticação e RBAC (Filtro Stateless) |
| **Token JWT** | JJWT (Java JWT) | 0.12.6 (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) |
| **Mensageria** | Spring Boot Starter AMQP (RabbitMQ) | RabbitMQ 4 com Exchange Direta |
| **Banco de Dados** | PostgreSQL | PostgreSQL 16 (H2 disponível para testes) |
| **Documentação API** | SpringDoc OpenAPI | 3.0.0 (Swagger UI v3) |
| **Serialização JSON** | Jackson | Manipulação e conversão de DTOs |
| **IA Generativa** | Google Generative Language API | Gemini (ex: `gemini-2.5-flash`) |
| **Produtividade** | Project Lombok | Redução de código boilerplate |
| **Testes** | JUnit 5, Mockito, AssertJ | Testes unitários com simulação de dependências |
| **Conteinerização** | Docker & Docker Compose | Orquestração da API, PostgreSQL e RabbitMQ |

---

## 📁 Estrutura do Projeto

O repositório está estruturado com a aplicação Spring Boot localizada no subdiretório `SupportFlow/SupportFlow`:

```
Spring-Boot-SupportFlow/
├── .idea/                                  # Metadados da IDE IntelliJ
├── SupportFlow/
│   └── SupportFlow/                        # Raiz do projeto Spring Boot / Maven
│       ├── .mvn/wrapper/                   # Binários do Maven Wrapper
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/project/SupportFlow/
│       │   │   │   ├── SupportFlowApplication.java  # Classe principal (SpringApplication)
│       │   │   │   ├── config/             # Configurações do ecossistema Spring
│       │   │   │   │   ├── RabbitMQConfig.java      # Declaração de fila, exchange e binding
│       │   │   │   │   ├── SecurityConfig.java      # Filtros, encoders e regras de rotas
│       │   │   │   │   ├── SecurityFilter.java      # Interceptador OncePerRequestFilter JWT
│       │   │   │   │   ├── SwaggerConfig.java       # Configuração OpenAPI e esquema Bearer
│       │   │   │   │   └── TokenProvider.java       # Geração e validação de tokens JWT
│       │   │   │   ├── controllers/        # Controladores REST da API
│       │   │   │   │   ├── AuthController.java      # Registro e login
│       │   │   │   │   ├── TicketController.java    # Operações de chamados e resposta de IA
│       │   │   │   │   └── UserController.java      # Operações administrativas de usuários
│       │   │   │   ├── dto/                # Records DTO de transferência de dados
│       │   │   │   │   ├── LoginRequestDTO.java
│       │   │   │   │   ├── TicketRequestDTO.java
│       │   │   │   │   ├── TicketResponseDTO.java
│       │   │   │   │   ├── TicketUpdateDTO.java
│       │   │   │   │   ├── UserRequestDTO.java
│       │   │   │   │   └── UserResponseDTO.java
│       │   │   │   ├── enums/              # Enumerações de domínio
│       │   │   │   │   ├── Role.java                # USER, ADMIN, SUPPORT
│       │   │   │   │   ├── TicketCategory.java      # ACCOUNT, BALANCE, PAYMENT, etc.
│       │   │   │   │   ├── TicketPriority.java      # LOW, MEDIUM, HIGH, CRITICAL
│       │   │   │   │   └── TicketStatus.java        # OPEN, IN_PROGRESS, RESOLVED, CLOSED
│       │   │   │   ├── exceptions/         # Exceções customizadas da aplicação
│       │   │   │   │   ├── AIResponseNotFoundException.java
│       │   │   │   │   ├── InvalidTicketTransiction.java
│       │   │   │   │   ├── TicketNotFoundException.java
│       │   │   │   │   └── UserNotFoundException.java
│       │   │   │   ├── handlers/           # Tratamento centralizado de erros
│       │   │   │   │   ├── ErrorResponse.java
│       │   │   │   │   └── GlobalExceptionHandler.java
│       │   │   │   ├── messaging/          # Camada de mensageria AMQP
│       │   │   │   │   ├── consumer/TicketConsumer.java # Listener da fila RabbitMQ
│       │   │   │   │   └── producer/TickerProducer.java # Envio para exchange RabbitMQ
│       │   │   │   ├── model/              # Entidades JPA mapeadas para o banco
│       │   │   │   │   ├── AIResponse.java
│       │   │   │   │   ├── Ticket.java
│       │   │   │   │   └── User.java
│       │   │   │   ├── repositories/       # Interfaces Spring Data JPA
│       │   │   │   │   ├── AIResponseRepository.java
│       │   │   │   │   ├── TicketRepository.java
│       │   │   │   │   └── UserRepository.java
│       │   │   │   └── service/            # Regras de negócio e integrações
│       │   │   │       ├── AIClient.java            # Cliente HTTP para Gemini API
│       │   │   │       ├── AIResponseService.java   # Orquestrador da resposta de IA
│       │   │   │       ├── AuthenticatedUser.java   # Helper de extração do usuário autenticado
│       │   │   │       ├── CustomUserDetailsService.java
│       │   │   │       ├── TicketService.java       # Lógica de ciclo de vida de tickets
│       │   │   │       └── UserService.java         # Lógica de usuários e senhas
│       │   │   └── resources/
│       │   │       └── application.yaml    # Configurações de banco, RabbitMQ, JWT e IA
│       │   └── test/
│       │       └── java/com/project/SupportFlow/
│       │           ├── SupportFlowApplicationTests.java # Teste de carregamento do contexto
│       │           └── service/
│       │               ├── TestTicketService.java       # Testes unitários do TicketService
│       │               └── TestUserService.java         # Testes unitários do UserService
│       ├── .env                            # Variáveis de ambiente
│       ├── docker-compose.yml              # Orquestração (App + Postgres + RabbitMQ)
│       ├── Dockerfile                      # Build multi-stage da aplicação
│       ├── mvnw / mvnw.cmd                 # Scripts do Maven Wrapper
│       └── pom.xml                         # Dependências e plugins Maven
└── README.md                               # Documentação do projeto
```

---

## 🚦 Regras de Negócio e Estados

### Categorias de Tickets (`TicketCategory`)
- `ACCOUNT`: Assuntos relacionados à conta do usuário.
- `BALANCE`: Questões relativas a saldo financeiro.
- `PAYMENT`: Problemas ou dúvidas sobre pagamentos.
- `TRANSFER`: Falhas ou conciliação em transferências.
- `TECHNICAL`: Dificuldades técnicas com sistemas/aplicativos.
- `OTHER`: Outras demandas de suporte geral.

### Níveis de Prioridade (`TicketPriority`)
- `LOW` | `MEDIUM` (padrão na criação) | `HIGH` | `CRITICAL`

### Regras de Transição de Status (`TicketStatus`)
O método `updateTicket` do `TicketService` aplica validações para manter a integridade do processo de atendimento:
1. **Não é permitido transitar de `OPEN` diretamente para `CLOSED`** (retorna `400 Bad Request`).
2. **Não é permitido transitar de `IN_PROGRESS` diretamente para `CLOSED`** (retorna `400 Bad Request`).
3. **Não é permitido reabrir um chamado fechado** (`CLOSED` para `OPEN`).
4. **Um chamado só pode ser alterado para `CLOSED` se o seu status atual for `RESOLVED`**.

---

## 📡 Endpoints da API

A URL base padrão da aplicação é `http://localhost:8080`.

### 1. Autenticação (`/v1/auth`)

| Método | Endpoint | Acesso | Descrição | Status de Sucesso |
|---|---|---|---|---|
| `POST` | `/v1/auth/register` | Público | Cadastra um novo usuário no sistema | `201 Created` |
| `POST` | `/v1/auth/login` | Público | Autentica as credenciais e devolve o token JWT | `202 Accepted` |

#### Exemplo de Requisição: Registro (`POST /v1/auth/register`)
```json
{
  "name": "Maria Silva",
  "email": "maria.silva@exemplo.com",
  "password": "senhaSegura123"
}
```

#### Exemplo de Requisição: Login (`POST /v1/auth/login`)
```json
{
  "email": "maria.silva@exemplo.com",
  "password": "senhaSegura123"
}
```
*Resposta:* String contendo o token JWT puro no corpo da resposta HTTP.

---

### 2. Chamados de Suporte (`/v1/ticket`)

> **Requer Header:** `Authorization: Bearer <seu_token_jwt>`

| Método | Endpoint | Acesso | Descrição | Status de Sucesso |
|---|---|---|---|---|
| `POST` | `/v1/ticket` | `ROLE_USER` | Cria um novo chamado e dispara evento assíncrono para triagem por IA | `201 Created` |
| `GET` | `/v1/ticket` | `ROLE_USER` | Lista todos os chamados pertencentes ao usuário autenticado | `200 OK` |
| `GET` | `/v1/ticket/{id}?id={id}` | `ROLE_USER` | Detalha um chamado específico (valida se pertence ao usuário) | `200 OK` |
| `PUT` | `/v1/ticket/{id}` | Autenticado | Atualiza informações e status do chamado obedecendo as regras de transição | `200 OK` |
| `DELETE` | `/v1/ticket/{id}` | Autenticado | Remove um chamado pelo seu ID | `204 No Content` |
| `GET` | `/v1/ticket/{id}/ai_response` | Autenticado | Obtém a análise técnica e sugestão gerada pela IA para o ticket | `200 OK` |

#### Exemplo de Requisição: Criação de Ticket (`POST /v1/ticket`)
```json
{
  "title": "Erro ao processar transferência Pix",
  "description": "Ao tentar realizar um Pix no valor de R$ 250, o app apresentou falha de comunicação com o provedor de pagamento.",
  "category": "TRANSFER"
}
```

#### Exemplo de Resposta: Criação de Ticket (`201 Created`)
```json
{
  "id": 1,
  "title": "Erro ao processar transferência Pix",
  "description": "Ao tentar realizar um Pix no valor de R$ 250, o app apresentou falha de comunicação com o provedor de pagamento.",
  "status": "OPEN",
  "priority": "MEDIUM",
  "category": "TRANSFER",
  "createdAt": "2026-11-08T18:00:00",
  "updatedAt": "2026-11-08T18:00:00"
}
```

#### Exemplo de Resposta: Consulta de Análise da IA (`GET /v1/ticket/1/ai_response`)
```json
{
  "id": 1,
  "response": "Análise Técnica:\n- Categoria: TRANSFER\n- Causa provável: Instabilidade temporária de gateway ou timeout na comunicação bancária.\n- Ação sugerida: Verificar logs de transação com o ID correlacionado, orientar o cliente a checar o extrato para evitar duplicidade e solicitar nova tentativa em caso de estorno.",
  "ticket": {
    "id": 1
  }
}
```

---

### 3. Gestão de Usuários (`/v1/users`)

> **Requer Header:** `Authorization: Bearer <token_com_role_admin>`

| Método | Endpoint | Acesso | Descrição | Status de Sucesso |
|---|---|---|---|---|
| `GET` | `/v1/users` | `ROLE_ADMIN` | Lista todos os usuários cadastrados | `200 OK` |
| `GET` | `/v1/users/{id}?id={id}` | `ROLE_ADMIN` | Obtém dados de um usuário pelo ID | `200 OK` |
| `PUT` | `/v1/users/{id}?id={id}` | `ROLE_ADMIN` | Atualiza os dados de um usuário | `200 OK` |
| `DELETE` | `/v1/users/{id}?id={id}` | `ROLE_ADMIN` | Exclui um usuário pelo ID | `204 No Content` |

---

## 🔐 Variáveis de Ambiente

As configurações sensíveis e dinâmicas da aplicação são carregadas via variáveis de ambiente definidas no arquivo `.env` e mapeadas no `application.yaml`:

| Variável | Descrição | Exemplo |
|---|---|---|
| `POSTGRES_URL` | URL de conexão JDBC com o PostgreSQL | `jdbc:postgresql://postgres:5432/supportflow` |
| `POSTGRES_USER` | Usuário do banco de dados | `postgres` |
| `POSTGRES_PASSWORD` | Senha do banco de dados | `postgres` |
| `JWT_SECRET` | Chave secreta HMAC-SHA para assinatura dos tokens JWT | *Chave alfanumérica de 256+ bits* |
| `JWT_EXPIRATION` | Tempo de expiração do token JWT em milissegundos | `900000` (15 minutos) |
| `AI_API_KEY` | Chave de acesso à API Google Generative AI (Gemini) | *Sua chave Google AI Studio* |
| `AI_API_MODEL` | Identificador do modelo Gemini a ser invocado | `gemini-2.5-flash` |

---

## ⚙️ Pré-requisitos

Para rodar o projeto, certifique-se de possuir instalado:
- **Git**
- **Docker** e **Docker Compose** (caso utilize containers)
- **Java JDK 21** e **Maven 3.9+** (caso opte por execução local sem Docker)
- Uma chave de API válida do **Google Gemini** ([Google AI Studio](https://aistudio.google.com/))

---

## 💻 Instalação e Execução

### Executando com Docker Compose (Recomendado)

A infraestrutura completa (API Spring Boot, banco de dados PostgreSQL e serviço de mensageria RabbitMQ) pode ser inicializada via Docker Compose:

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/igorprj/SpringBoot--Support-Flow-Java.git
   cd SpringBoot--Support-Flow-Java
   ```

2. **Navegue até o diretório da aplicação e configure o `.env`:**
   ```bash
   cd SupportFlow/SupportFlow
   ```
   Crie ou edite o arquivo `.env` preenchendo sua chave da API do Gemini e segredo JWT:
   ```env
   AI_API_KEY=sua_chave_gemini_aqui
   AI_API_MODEL=gemini-2.5-flash
   JWT_SECRET=seu_segredo_jwt_longo_e_seguro_aqui_123456
   JWT_EXPIRATION=900000
   ```

3. **Inicie os serviços:**
   ```bash
   docker compose up --build -d
   ```

4. **Verifique o status dos containers:**
   ```bash
   docker compose ps
   ```
   Os seguintes serviços estarão disponíveis:
   - **SupportFlow API:** `http://localhost:8080`
   - **PostgreSQL:** `localhost:5432`
   - **RabbitMQ Management Dashboard:** `http://localhost:15672` (login: `guest` / `guest`)

5. **Para encerrar a execução:**
   ```bash
   docker compose down
   ```

---

### Executando Localmente

Caso prefira rodar a aplicação localmente pelo Maven Wrapper com PostgreSQL e RabbitMQ externos:

1. **Inicie as instâncias do PostgreSQL e RabbitMQ:**
   Você pode subir apenas os serviços de apoio via Docker:
   ```bash
   docker compose up -d postgres rabbitmq
   ```

2. **Defina as variáveis de ambiente necessárias no seu terminal:**
   *No Linux/macOS:*
   ```bash
   export POSTGRES_URL=jdbc:postgresql://localhost:5432/supportflow
   export POSTGRES_USER=postgres
   export POSTGRES_PASSWORD=postgres
   export JWT_SECRET=seu_segredo_jwt_longo_e_seguro_aqui_123456
   export JWT_EXPIRATION=900000
   export AI_API_KEY=sua_chave_gemini_aqui
   export AI_API_MODEL=gemini-2.5-flash
   ```

   *No Windows (PowerShell):*
   ```powershell
   $env:POSTGRES_URL="jdbc:postgresql://localhost:5432/supportflow"
   $env:POSTGRES_USER="postgres"
   $env:POSTGRES_PASSWORD="postgres"
   $env:JWT_SECRET="seu_segredo_jwt_longo_e_seguro_aqui_123456"
   $env:JWT_EXPIRATION="900000"
   $env:AI_API_KEY="sua_chave_gemini_aqui"
   $env:AI_API_MODEL="gemini-2.5-flash"
   ```

3. **Execute a aplicação pelo Maven:**
   *No Linux/macOS:*
   ```bash
   ./mvnw spring-boot:run
   ```
   *No Windows:*
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

---

## 🧪 Testes Automatizados

O projeto conta com suíte de testes unitários construída com **JUnit 5**, **Mockito** e **AssertJ**, cobrindo regras de negócio e serviços:

- `TestUserService`:
  - Criação de usuário com codificação de senha e persistência.
  - Listagem completa e tratamento de lista vazia.
  - Busca por ID com sucesso e validação de exceção para usuário inexistente.
  - Atualização cadastral de dados do usuário.
  - Exclusão com validação de chamada ao repositório.
- `TestTicketService`:
  - Criação de chamado com valores padrão (`status: OPEN`, `priority: MEDIUM`).
  - Listagem de tickets cadastrados.
  - Consulta por ID e validação de tickets inexistentes.
  - Atualização de dados cadastrais do chamado.
  - Exclusão de tickets.
- `SupportFlowApplicationTests`:
  - Validação do carregamento de contexto Spring Boot.

Para rodar os testes a partir da pasta `SupportFlow/SupportFlow`:
```bash
./mvnw test
```
*(No Windows, utilize `.\mvnw.cmd test`)*

---

## 📖 Documentação Interativa (Swagger/OpenAPI)

Com a aplicação em execução, a documentação interativa e especificação técnica OpenAPI 3 estão acessíveis em:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

> **Como testar rotas autenticadas no Swagger UI:**
> 1. Realize o login no endpoint `/v1/auth/login`.
> 2. Copie o token retornado no corpo da resposta.
> 3. Clique no botão verde **Authorize** no canto superior direito do Swagger UI.
> 4. Cole o token no campo de texto e clique em **Authorize**. Todas as requisições subsequentes incluirão o cabeçalho `Authorization: Bearer <token>`.
