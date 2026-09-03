DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE pessoa (
    id SERIAL PRIMARY KEY,
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
    id INT PRIMARY KEY REFERENCES pessoa(id),
    dividas_abertas BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE funcionario (
    id INT PRIMARY KEY REFERENCES pessoa(id),
    salario NUMERIC(19, 4) NOT NULL,
    turno VARCHAR(20) NOT NULL,
    cargo VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ativo'
);

CREATE TABLE caixa_de_agua (
    id SERIAL PRIMARY KEY,
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

CREATE TABLE caixa(
    id SERIAL PRIMARY KEY,
    saldo NUMERIC(19, 4) NOT NULL
);

INSERT INTO caixa(saldo) VALUES (100000);

CREATE TABLE movimentacao(
    id SERIAL PRIMARY KEY,
    id_caixa INT REFERENCES caixa(id),
    id_produto INT REFERENCES caixa_de_agua(id),
    quantidade INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transacao(
    id SERIAL PRIMARY KEY,
    id_caixa INT NOT NULL REFERENCES caixa(id),
    id_pessoa INT NOT NULL REFERENCES pessoa(id),
    valor NUMERIC(19, 4),
    tipo VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    status VARCHAR(30),
    saldo_anterior NUMERIC(19, 4) NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION validar_saldo_transacao()
    RETURNS TRIGGER AS $$
DECLARE
    saldo_atual DECIMAL;
BEGIN
    IF NEW.tipo = 'SAIDA' THEN
        SELECT saldo INTO saldo_atual FROM caixa WHERE id = 1;
        NEW.saldo_anterior := saldo_atual;
        IF saldo_atual >= NEW.valor THEN
            UPDATE caixa SET saldo = saldo - NEW.valor WHERE id = 1;
            NEW.status := 'CONCLUIDA';
        ELSE
            NEW.status := 'CANCELADA';
        END IF;
    ELSE
        UPDATE caixa SET saldo = saldo + NEW.valor WHERE id = 1;
        NEW.status := 'CONCLUIDA';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validar_saldo
    BEFORE INSERT ON transacao
    FOR EACH ROW
EXECUTE FUNCTION validar_saldo_transacao();
