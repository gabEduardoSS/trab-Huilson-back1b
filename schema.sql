-- Script de criação das tabelas usadas pelo projeto.
-- Antes o Hibernate criava isso sozinho (hibernate.hbm2ddl.auto=update).
-- Em JDBC puro isso não existe mais, então esse schema precisa ser criado manualmente.

CREATE TABLE pessoa (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(20),
    cidade VARCHAR(255),
    endereco VARCHAR(255),
    dt_nasc DATE,
    tipo VARCHAR(20) NOT NULL,
    dt_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cliente (
    id BIGINT PRIMARY KEY REFERENCES pessoa(id),
    dividas_abertas BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE funcionario (
    id BIGINT PRIMARY KEY REFERENCES pessoa(id),
    salario NUMERIC(10, 2) NOT NULL,
    turno VARCHAR(20) NOT NULL,
    cargo VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ativo'
);

CREATE TABLE caixa_de_agua (
    id BIGSERIAL PRIMARY KEY,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    dimensao FLOAT8[] NOT NULL,
    cor VARCHAR(20) NOT NULL,
    material VARCHAR(30) NOT NULL,
    formato VARCHAR(20) NOT NULL,
    fornecedor VARCHAR(255),
    preco NUMERIC(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ativo',
    quantidade INT NOT NULL,
    quantidade_minima INT,
    quantidade_maxima INT,
    dt_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

