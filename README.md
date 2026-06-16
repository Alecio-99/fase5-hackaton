# Hackathon SUS — API de Triagem, Fila Inteligente e Antifaltas

> Tech Challenge — Fase 5 (Hackathon) · Pós-graduação FIAP em Arquitetura e Desenvolvimento Java (turma 9ADJT)

API REST que apoia a **otimização do atendimento no SUS**, atacando três gargalos reais das unidades de saúde: **superlotação** (triagem com classificação de risco), **filas de espera** (fila priorizada por gravidade) e **absenteísmo** (controle de faltas em consultas e dashboard gerencial).

O escopo cobre apenas **back-end e arquitetura**, conforme o enunciado do hackathon (front-end não é exigido).

---

## Sumário

- [Problema e solução](#problema-e-solução)
- [Principais funcionalidades](#principais-funcionalidades)
- [Arquitetura](#arquitetura)
- [Stack tecnológica](#stack-tecnológica)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Configuração (variáveis de ambiente)](#configuração-variáveis-de-ambiente)
- [Autenticação](#autenticação)
- [Documentação da API](#documentação-da-api)
- [Endpoints principais](#endpoints-principais)
- [Regra de classificação de risco](#regra-de-classificação-de-risco)
- [Modelo de dados](#modelo-de-dados)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)

---

## Problema e solução

O SUS enfrenta sobrecarga de atendimentos, filas longas e alto índice de faltas em consultas agendadas (absenteísmo), o que desperdiça vagas escassas. Esta API oferece:

- **Triagem inteligente** — classifica automaticamente o risco do paciente (protocolo inspirado no Manchester) a partir de respostas objetivas, direcionando os casos mais urgentes primeiro.
- **Fila priorizada** — organiza o atendimento por gravidade e ordem de chegada, em vez de simples ordem de fila.
- **Gestão de consultas e antifaltas** — agendamento, confirmação, registro de comparecimento/falta e cálculo da taxa de absenteísmo.
- **Lista de espera** — controle de pacientes aguardando vaga por especialidade e prioridade, com histórico de movimentações.
- **Dashboard gerencial** — indicadores para apoiar a tomada de decisão das unidades.

## Principais funcionalidades

| Módulo | Descrição |
|---|---|
| **Auth** | Login e registro com JWT; perfis `ADMIN`, `PROFISSIONAL_SAUDE`, `RECEPCAO`. |
| **Pacientes** | CRUD completo e busca por CPF. |
| **Unidades de Saúde** | CRUD de unidades (`UBS`, `UPA`, `HOSPITAL`). |
| **Triagem** | Registro de triagem com classificação automática de risco. |
| **Fila** | Entrada, consulta de posição, chamada do próximo (por prioridade), finalização e cancelamento. |
| **Consultas** | Agendamento, confirmação, cancelamento, registro de falta e de comparecimento. |
| **Lista de Espera** | Cadastro e listagem por especialidade/prioridade. |
| **Dashboard** | Estatísticas: pacientes, consultas, taxa de absenteísmo, fila e atendimentos por classificação de risco. |

## Arquitetura

O projeto adota **Arquitetura Hexagonal (Ports & Adapters)**, isolando o domínio das tecnologias de infraestrutura. A API segue a abordagem **OpenAPI-first**: as interfaces dos controllers são geradas a partir do contrato `openapi.yaml`.

```
┌──────────────────────────────────────────────────────────┐
│  presentation  → controllers (resources), mappers,       │
│                  handler global de exceções              │
├──────────────────────────────────────────────────────────┤
│  application   → services (casos de uso) + ports         │
│                  (interfaces de repositório)             │
├──────────────────────────────────────────────────────────┤
│  domain        → modelos, enums, regras de negócio       │
│                  (ex.: ClassificadorRiscoService)        │
├──────────────────────────────────────────────────────────┤
│  infrastructure→ adapters JPA, entities, security (JWT), │
│                  persistência (MySQL + Flyway)           │
└──────────────────────────────────────────────────────────┘
```

Camadas:
- **domain** — entidades de negócio puras e a regra de classificação de risco, sem dependência de framework.
- **application** — serviços de caso de uso e *ports* (contratos de repositório).
- **infrastructure** — adapters de persistência (JPA/Hibernate), segurança (Spring Security + JWT) e migrations.
- **presentation** — controllers REST, mapeamento DTO ↔ domínio e tratamento centralizado de erros.

## Stack tecnológica

- **Java 17**
- **Spring Boot 3.3.7** — Web, Data JPA, Security, Validation
- **MySQL** (produção/dev) · **H2** (testes)
- **Flyway** — versionamento de schema
- **JWT (jjwt)** — autenticação stateless
- **springdoc-openapi** + **openapi-generator** — contrato e Swagger UI
- **Maven** (com wrapper `mvnw`)

## Pré-requisitos

- JDK 17+
- Maven 3.9+ (ou use o wrapper `./mvnw`)
- MySQL 8 em execução (ou ajuste `DB_URL` para sua instância)

## Como executar

### 1. Subir o banco MySQL

A aplicação espera um MySQL acessível. Por padrão usa `jdbc:mysql://localhost:3306/hackton` (o schema é criado automaticamente se não existir). Exemplo rápido com Docker:

```bash
docker run --name mysql-hackton -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=hackton -p 3306:3306 -d mysql:8
```

### 2. Executar a aplicação

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows (PowerShell)
.\mvnw.cmd spring-boot:run
```

As migrations do Flyway criam todas as tabelas no primeiro start. A API ficará disponível em:

```
http://localhost:8080/api/v1
```

### 3. (Opcional) Empacotar

```bash
./mvnw clean package
java -jar target/hackton-0.0.1-SNAPSHOT.jar
```

## Configuração (variáveis de ambiente)

Todas têm valores padrão para desenvolvimento — sobrescreva em produção.

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/hackton?...` | URL JDBC do MySQL |
| `DB_USERNAME` | `root` | Usuário do banco |
| `DB_PASSWORD` | `root` | Senha do banco |
| `JWT_SECRET` | *(chave de dev)* | Segredo HMAC do JWT (≥ 256 bits) |
| `JWT_EXPIRATION_MINUTES` | `120` | Validade do token em minutos |
| `APP_ADMIN_EMAIL` | `admin@sus.local` | E-mail do admin criado no 1º start |
| `APP_ADMIN_PASSWORD` | `admin123` | Senha do admin criado no 1º start |

> Na primeira inicialização, se não houver usuários, um administrador padrão é criado automaticamente (`admin@sus.local` / `admin123`). **Altere essas credenciais em qualquer ambiente real.**

## Autenticação

A API é stateless e protegida por **JWT**. Fluxo:

1. Faça login para obter o token:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@sus.local","senha":"admin123"}'
```

2. Use o token retornado no header das demais chamadas:

```bash
curl http://localhost:8080/api/v1/pacientes \
  -H "Authorization: Bearer <SEU_TOKEN>"
```

As rotas `/auth/login` e `/auth/register` são públicas; todas as demais exigem o header `Authorization: Bearer <token>`.

## Documentação da API

- **Swagger UI:** `http://localhost:8080/api/v1/swagger-ui.html`
- **Contrato OpenAPI:** [`src/main/resources/openapi.yaml`](src/main/resources/openapi.yaml)
- **Coleção Postman:** [`hackton-sus.postman_collection.json`](hackton-sus.postman_collection.json) (importe no Postman para testar todos os endpoints)

## Endpoints principais

Base: `/api/v1`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Autenticar e obter token |
| `POST` | `/auth/register` | Registrar usuário |
| `GET/POST` | `/pacientes` | Listar / criar pacientes |
| `GET/PUT/DELETE` | `/pacientes/{id}` | Buscar / atualizar / excluir |
| `GET` | `/pacientes/cpf/{cpf}` | Buscar paciente por CPF |
| `GET/POST` | `/unidades-saude` | Listar / criar unidades |
| `GET/PUT/DELETE` | `/unidades-saude/{id}` | Buscar / atualizar / excluir |
| `POST` | `/triagens` | Registrar triagem (classifica risco) |
| `GET` | `/triagens/{id}` | Buscar triagem |
| `POST` | `/fila` | Entrar na fila |
| `GET` | `/fila/{id}/posicao` | Consultar posição na fila |
| `POST` | `/fila/unidades/{unidadeId}/proximo` | Chamar próximo paciente (por prioridade) |
| `POST` | `/fila/{id}/finalizar` · `/fila/{id}/cancelar` | Encerrar atendimento |
| `POST` | `/consultas` | Agendar consulta |
| `POST` | `/consultas/{id}/confirmar` · `/cancelar` · `/falta` · `/comparecimento` | Gerenciar consulta |
| `GET/POST` | `/lista-espera` | Listar / cadastrar na lista de espera |
| `GET` | `/dashboard` | Estatísticas gerenciais |

## Regra de classificação de risco

A triagem recebe respostas booleanas e devolve uma classificação no padrão do Protocolo de Manchester:

| Condição (na ordem de avaliação) | Classificação |
|---|---|
| Falta de ar **ou** sangramento | 🔴 `VERMELHO` |
| Dor intensa | 🟠 `LARANJA` |
| Febre **e** tontura | 🟡 `AMARELO` |
| Febre **ou** tontura | 🟢 `VERDE` |
| Nenhum dos sintomas | 🔵 `AZUL` |

Implementação em [`ClassificadorRiscoService`](src/main/java/com/fase5/hackton/domain/service/ClassificadorRiscoService.java).

## Modelo de dados

Tabelas criadas pela migration [`V1__create_core_schema.sql`](src/main/resources/db/migration/V1__create_core_schema.sql):

`usuarios` · `pacientes` · `unidades_saude` · `triagens` · `fila_atendimento` · `consultas` · `lista_espera` · `historico_lista_espera`

Há índices dedicados para priorização da fila (`idx_fila_prioridade`) e da lista de espera (`idx_lista_espera_prioridade`).

## Testes

```bash
./mvnw test
```

Os testes de domínio rodam sobre H2 (sem necessidade de MySQL). Cobertura atual: carga de contexto da aplicação e regra de classificação de risco.

## Estrutura do projeto

```
src/main/java/com/fase5/hackton/
├── application/        # services (casos de uso) + ports
├── domain/             # modelos, enums, exceções, regra de risco
├── infrastructure/
│   ├── persistence/    # entities JPA, adapters, repositories, mappers
│   └── security/       # JWT, filtros, SecurityConfig, admin inicial
└── presentation/       # resources (controllers), mappers, handler de erros

src/main/resources/
├── application.yml     # configuração
├── openapi.yaml        # contrato da API
└── db/migration/       # scripts Flyway
```

---

**Desenvolvido por Giovana Leite Scalabrini e Alecio Silveira Araújo - Turma 9ADJT — FIAP** · Hackaton Fase 5
