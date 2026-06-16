# Hackathon SUS — API de Triagem, Fila Inteligente e Antifaltas

> Tech Challenge — Fase 5 (Hackathon) · Pós-graduação FIAP em Arquitetura e Desenvolvimento Java (turma 9ADJT)

API REST que apoia a **otimização do atendimento no SUS**, atacando três gargalos reais das unidades de saúde: **superlotação** (triagem com classificação de risco), **filas de espera** (fila priorizada por gravidade) e **absenteísmo** (controle de faltas em consultas e dashboard gerencial).

O escopo cobre apenas **back-end e arquitetura**, conforme o enunciado do hackathon (front-end não é exigido).

---

## Sumário

- [1. Resumo executivo](#1-resumo-executivo)
- [2. Problema identificado](#2-problema-identificado)
- [3. Descrição da solução](#3-descrição-da-solução)
- [4. Processo de desenvolvimento](#4-processo-de-desenvolvimento)
- [5. Detalhes técnicos](#5-detalhes-técnicos)
- [6. Links úteis](#6-links-úteis)
- [7. Aprendizados e próximos passos](#7-aprendizados-e-próximos-passos)
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

## 1. Resumo executivo

O atendimento no SUS sofre com superlotação das unidades, filas longas e desorganizadas e alto índice de faltas em consultas agendadas (absenteísmo), o que desperdiça vagas escassas e atrasa quem realmente precisa. Esta solução é uma **API REST de back-end** que ataca três gargalos de forma integrada:

- **Triagem inteligente** — classifica automaticamente o risco do paciente (protocolo inspirado no Manchester) a partir de respostas objetivas, direcionando os casos mais urgentes primeiro.
- **Fila priorizada** — organiza o atendimento por gravidade clínica e ordem de chegada, em vez de simples ordem de fila.
- **Antifaltas com reaproveitamento de vagas** — controla comparecimento/falta, calcula a taxa de absenteísmo e, ao cancelar uma consulta, reagenda automaticamente o próximo paciente da lista de espera.

O impacto esperado é a redução do tempo de espera dos casos urgentes, o melhor aproveitamento das vagas disponíveis e o apoio à gestão por meio de um dashboard de indicadores.

## 2. Problema identificado

O SUS enfrenta desafios crônicos no fluxo de atendimento:

- **Superlotação e triagem manual:** sem priorização sistemática, pacientes graves podem aguardar o mesmo tempo que casos leves, elevando o risco clínico.
- **Filas desorganizadas:** o atendimento por ordem simples de chegada não reflete a gravidade dos pacientes.
- **Absenteísmo elevado:** faltas em consultas agendadas desperdiçam vagas que poderiam atender outras pessoas da lista de espera; em diversas especialidades do setor público as taxas de falta superam 20% a 30%.
- **Falta de visibilidade gerencial:** gestores carecem de indicadores consolidados (pacientes em fila, taxa de absenteísmo, distribuição por risco) para apoiar decisões.

**Justificativa:** resolver esses pontos melhora diretamente a segurança do paciente (priorização correta), a eficiência operacional (aproveitamento de vagas) e a transparência da gestão — exatamente os objetivos do tema do hackathon.

## 3. Descrição da solução

A solução é uma API REST que cobre o ciclo de atendimento, organizada nos seguintes módulos:

| Módulo | Descrição |
|---|---|
| **Auth** | Login e registro com JWT; perfis `ADMIN`, `PROFISSIONAL_SAUDE`, `RECEPCAO`. |
| **Pacientes** | CRUD completo e busca por CPF. |
| **Unidades de Saúde** | CRUD de unidades (`UBS`, `UPA`, `HOSPITAL`). |
| **Triagem** | Registro de triagem com classificação automática de risco. |
| **Fila** | Entrada, consulta de posição, chamada do próximo (por prioridade), finalização e cancelamento. |
| **Consultas** | Agendamento, confirmação, cancelamento, registro de falta e de comparecimento. |
| **Lista de Espera** | Cadastro e listagem por especialidade/prioridade, com histórico de movimentações. |
| **Dashboard** | Estatísticas: pacientes, consultas, taxa de absenteísmo, fila e atendimentos por classificação de risco. |

Destaques de como a solução atende ao problema:

- A **classificação de risco** é automática a partir de sintomas objetivos (ver [Regra de classificação de risco](#regra-de-classificação-de-risco)).
- A **priorização da fila** seleciona, ao chamar o próximo paciente, quem possui maior gravidade e, em empate, o que chegou primeiro.
- O **antifaltas com reaproveitamento de vaga** atua no cancelamento de uma consulta: a API busca o próximo paciente da lista de espera da mesma especialidade, cria automaticamente uma nova consulta reaproveitando o horário, registra a movimentação no histórico e remove o paciente da lista de espera.

## 4. Processo de desenvolvimento

O trabalho foi estruturado em etapas incrementais:

1. **Entendimento do problema (Design Thinking):** mapeamento das dores do SUS (superlotação, filas, absenteísmo) e definição das personas — recepcionista, profissional de saúde e gestor da unidade.
2. **Brainstorming e priorização:** seleção das funcionalidades de maior impacto e viabilidade para o MVP (triagem, fila, antifaltas).
3. **Desenho da arquitetura:** adoção de Arquitetura Hexagonal (Ports & Adapters) e abordagem API-first com contrato OpenAPI.
4. **Desenvolvimento ágil:** implementação em ciclos curtos por módulo, com o contrato OpenAPI gerando as interfaces dos controllers e o domínio isolado de framework.
5. **Testes:** testes unitários dos serviços de aplicação e da regra de classificação de risco (executando em banco H2).
6. **Documentação e deploy:** contrato OpenAPI/Swagger, coleção Postman, diagramas, containerização (Docker) e script de deploy automatizado.

## 5. Detalhes técnicos

### Stack tecnológica

- **Java 17**
- **Spring Boot 3.3.7** — Web, Data JPA, Security, Validation
- **MySQL** (produção/dev) · **H2** (testes)
- **Flyway** — versionamento de schema
- **JWT (jjwt)** — autenticação stateless
- **springdoc-openapi** + **openapi-generator** — contrato e Swagger UI
- **Maven** (com wrapper `mvnw`)
- **Docker** e **Docker Compose** — containerização (API + MySQL)

### Arquitetura

O projeto adota **Arquitetura Hexagonal (Ports & Adapters)**, isolando o domínio das tecnologias de infraestrutura. A API segue a abordagem **OpenAPI-first**: as interfaces dos controllers são geradas a partir do contrato `openapi.yaml`.

Camadas:
- **domain** — entidades de negócio puras e a regra de classificação de risco, sem dependência de framework.
- **application** — serviços de caso de uso e *ports* (contratos de repositório).
- **infrastructure** — adapters de persistência (JPA/Hibernate), segurança (Spring Security + JWT) e migrations.
- **presentation** — controllers REST, mapeamento DTO ↔ domínio e tratamento centralizado de erros.

![Arquitetura hexagonal da solução](docs/images/Arquitetura%20v1.png)

### Fluxos principais

**Triagem com classificação de risco e entrada na fila priorizada:**

![Fluxo de triagem e fila](docs/images/Diagrama%20de%20Sequencia%20-%20Triagem%20Fila.png)

**Cancelamento de consulta com reagendamento automático da lista de espera:**

![Fluxo de cancelamento e reagendamento](docs/images/Diagrama%20de%20Sequencia%20-%20Cancelamento.png)

> As fontes editáveis dos diagramas (PlantUML) estão em [`docs/`](docs): `arquitetura.puml`, `fluxo-triagem-fila.puml` e `fluxo-cancelamento-reagendamento.puml`.

## 6. Links úteis

- **Repositório de código (GitHub):** https://github.com/Alecio-99/fase5-hackaton/tree/main
- **Vídeo do pitch (YouTube, 8 min):** _a ser publicado_ — `https://youtu.be/ID_DO_VIDEO`
- **Contrato OpenAPI:** [`src/main/resources/openapi.yaml`](src/main/resources/openapi.yaml)
- **Coleção Postman:** [`hackton-sus.postman_collection.json`](hackton-sus.postman_collection.json)

## 7. Aprendizados e próximos passos

**O que a equipe aprendeu:**
- **Arquitetura Hexagonal na prática:** isolar o domínio das tecnologias facilitou os testes e mantém o núcleo de negócio independente de framework e banco.
- **API-first com OpenAPI:** definir o contrato antes acelerou a implementação dos controllers e padronizou os DTOs e respostas de erro.
- **Modelagem de regras clínicas:** traduzir um protocolo de triagem (Manchester) em regras objetivas e testáveis reforçou a importância de uma camada de domínio limpa.
- **Valor de pequenas automações:** o reagendamento automático a partir do cancelamento mostrou como uma regra simples gera impacto real no aproveitamento de vagas.

**O que pode ser aprimorado no futuro:**
- Enriquecer a triagem com sinais vitais (pressão, saturação), idade e comorbidades.
- Notificações (SMS, e-mail ou WhatsApp) de lembrete de consulta para reduzir ainda mais o absenteísmo.
- Integração com telemedicina e prontuário eletrônico centralizado.
- Observabilidade (métricas e logs estruturados) e pipeline de CI/CD.

---

## 8. Como executar

### Opção A — Docker (recomendado)

Forma mais simples: sobe **API + MySQL** em containers com um único comando.

**Pré-requisitos:** Docker e Docker Compose instalados e com o daemon em execução.

> ⚠️ **A porta `3306` precisa estar livre antes de executar.** O MySQL do projeto é publicado nessa porta; se você já tiver um MySQL local rodando, pare-o (ou ajuste `DB_PORT` no arquivo `.env`) antes de continuar.

```bash
./deploy.sh
```

O script executa, em ordem, duas etapas:

1. **Build** ([`scripts/build.sh`](scripts/build.sh)) — `mvn clean install` com os testes.
2. **Deploy** ([`scripts/docker.sh`](scripts/docker.sh)) — build das imagens, `docker compose up` e health check da API.

Ao final, a aplicação estará disponível em `http://localhost:8080/api/v1`.

Outras opções do script:

```bash
./deploy.sh --no-tests   # pula os testes na etapa de build
./deploy.sh --down       # derruba os containers e volumes
```

### Opção B — Execução local (sem Docker)

**Pré-requisitos:** JDK 17+, Maven 3.9+ (ou o wrapper `./mvnw`) e um MySQL 8 acessível.

```bash
# 1. Subir um MySQL (exemplo com Docker)
docker run --name mysql-hackton -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=hackton -p 3306:3306 -d mysql:8

# 2. Executar a aplicação
./mvnw spring-boot:run          # Linux/macOS
.\mvnw.cmd spring-boot:run      # Windows (PowerShell)
```

As migrations do Flyway criam todas as tabelas no primeiro start. A API ficará disponível em `http://localhost:8080/api/v1`.

Para empacotar e rodar o JAR:

```bash
./mvnw clean package
java -jar target/hackton-0.0.1-SNAPSHOT.jar
```

## Configuração (variáveis de ambiente)

Todas têm valores padrão para desenvolvimento — sobrescreva em produção. No fluxo Docker, são lidas do arquivo `.env` (criado automaticamente a partir de [`.env.example`](.env.example) na primeira execução do `deploy.sh`).

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/hackton?...` | URL JDBC do MySQL (execução local) |
| `DB_USERNAME` | `root` | Usuário do banco |
| `DB_PASSWORD` | `root` | Senha do banco |
| `DB_PORT` | `3306` | Porta publicada do MySQL (fluxo Docker) |
| `APP_PORT` | `8080` | Porta publicada da API (fluxo Docker) |
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

A rota `/auth/login` é pública; as demais exigem o header `Authorization: Bearer <token>` e respeitam o perfil do usuário.

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

Os testes rodam sobre H2 (sem necessidade de MySQL). A suíte cobre os serviços de aplicação (Auth, Paciente, Unidade, Triagem, Fila, Consulta, Lista de Espera e Dashboard) — incluindo a priorização da fila, o cálculo da taxa de absenteísmo e o reagendamento automático — além da regra de classificação de risco e da carga de contexto da aplicação.

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

docs/                   # diagramas e relatório
scripts/                # build.sh e docker.sh (usados pelo deploy.sh)
Dockerfile · docker-compose.yml · deploy.sh · .env.example
```

---

**Desenvolvido por Giovana Leite Scalabrini e Alecio Silveira Araújo - Turma 9ADJT — FIAP** · Hackaton Fase 5
