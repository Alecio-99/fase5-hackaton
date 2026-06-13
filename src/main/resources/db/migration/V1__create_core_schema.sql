create table usuarios (
    id bigint not null auto_increment,
    nome varchar(255) not null,
    email varchar(255) not null,
    senha_hash varchar(255) not null,
    perfil varchar(40) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint uk_usuarios_email unique (email)
);

create table pacientes (
    id bigint not null auto_increment,
    nome varchar(255) not null,
    cpf varchar(11) not null,
    data_nascimento date not null,
    telefone varchar(255) not null,
    email varchar(255),
    endereco varchar(255) not null,
    cartao_sus varchar(255) not null,
    data_cadastro timestamp(6) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint uk_pacientes_cpf unique (cpf),
    constraint uk_pacientes_cartao_sus unique (cartao_sus)
);

create table unidades_saude (
    id bigint not null auto_increment,
    nome varchar(255) not null,
    tipo varchar(40) not null,
    endereco varchar(255) not null,
    telefone varchar(255) not null,
    capacidade_atendimento integer not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id)
);

create table triagens (
    id bigint not null auto_increment,
    paciente_id bigint not null,
    data_triagem timestamp(6) not null,
    possui_febre boolean not null,
    possui_falta_de_ar boolean not null,
    possui_dor_intensa boolean not null,
    possui_sangramento boolean not null,
    possui_tontura boolean not null,
    classificacao_risco varchar(40) not null,
    observacoes varchar(1000),
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint fk_triagens_paciente foreign key (paciente_id) references pacientes (id)
);

create table fila_atendimento (
    id bigint not null auto_increment,
    paciente_id bigint not null,
    unidade_saude_id bigint not null,
    classificacao_risco varchar(40) not null,
    horario_entrada timestamp(6) not null,
    status varchar(40) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint fk_fila_paciente foreign key (paciente_id) references pacientes (id),
    constraint fk_fila_unidade foreign key (unidade_saude_id) references unidades_saude (id)
);

create index idx_fila_prioridade on fila_atendimento (unidade_saude_id, status, classificacao_risco, horario_entrada);

create table consultas (
    id bigint not null auto_increment,
    paciente_id bigint not null,
    unidade_saude_id bigint not null,
    data_hora_consulta timestamp(6) not null,
    especialidade varchar(255) not null,
    status varchar(40) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint fk_consultas_paciente foreign key (paciente_id) references pacientes (id),
    constraint fk_consultas_unidade foreign key (unidade_saude_id) references unidades_saude (id)
);

create table lista_espera (
    id bigint not null auto_increment,
    paciente_id bigint not null,
    especialidade varchar(255) not null,
    prioridade integer not null,
    data_cadastro timestamp(6) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint fk_lista_espera_paciente foreign key (paciente_id) references pacientes (id)
);

create index idx_lista_espera_prioridade on lista_espera (especialidade, prioridade, data_cadastro);

create table historico_lista_espera (
    id bigint not null auto_increment,
    paciente_id bigint not null,
    consulta_id bigint,
    especialidade varchar(255) not null,
    descricao varchar(1000) not null,
    data_movimentacao timestamp(6) not null,
    created_at timestamp(6) not null,
    updated_at timestamp(6) not null,
    primary key (id),
    constraint fk_historico_lista_paciente foreign key (paciente_id) references pacientes (id),
    constraint fk_historico_lista_consulta foreign key (consulta_id) references consultas (id)
);
