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
    dt_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE caixa(
    id SERIAL PRIMARY KEY,
    saldo NUMERIC(19, 4) NOT NULL
);

INSERT INTO caixa(saldo) VALUES (100000);

CREATE TABLE transacao(
    id SERIAL PRIMARY KEY,
    id_caixa INT NOT NULL REFERENCES caixa(id) NOT NULL,
    id_pessoa INT NOT NULL REFERENCES pessoa(id) NOT NULL,
    valor NUMERIC(19, 4),
    tipo VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    status VARCHAR(30),
    saldo_anterior NUMERIC(19, 4) NOT NULL,
    saldo_posterior NUMERIC(19, 4) NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movimentacao(
    id SERIAL PRIMARY KEY,
    id_produto INT REFERENCES caixa_de_agua(id) NOT NULL,
    quantidade INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    status VARCHAR(30),
    quantidade_anterior INT NOT NULL,
    quantidade_posterior INT NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION validar_saldo_transacao()
    RETURNS TRIGGER AS $$
DECLARE
    saldo_atual DECIMAL;
    saldo_novo DECIMAL;
BEGIN
    SELECT saldo INTO saldo_atual FROM caixa WHERE id = 1;
    NEW.saldo_anterior := saldo_atual;
    IF NEW.tipo = 'SAIDA' THEN
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
    SELECT saldo INTO saldo_novo FROM caixa WHERE id = 1;
    NEW.saldo_posterior := saldo_novo;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION validar_quantidade_movimentacao()
    RETURNS TRIGGER AS $$
DECLARE
    quantidade_atual INT;
    quantidade_nova INT;
BEGIN
    SELECT quantidade INTO quantidade_atual FROM caixa_de_agua WHERE id = NEW.id_produto;
    NEW.quantidade_anterior := quantidade_atual;
    IF NEW.tipo = 'SAIDA' THEN
        IF quantidade_atual >= NEW.quantidade THEN
            UPDATE caixa_de_agua SET quantidade = caixa_de_agua.quantidade - NEW.quantidade WHERE id = NEW.id_produto;
            NEW.status := 'CONCLUIDA';
        ELSE
            NEW.status := 'CANCELADA';
        END IF;
    ELSE
        UPDATE caixa_de_agua SET quantidade = quantidade + NEW.quantidade WHERE id = NEW.id_produto;
        NEW.status := 'CONCLUIDA';
    END IF;
    SELECT quantidade INTO quantidade_nova FROM caixa_de_agua WHERE id = NEW.id_produto;
    NEW.quantidade_posterior := quantidade_nova;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validar_saldo
    BEFORE INSERT ON transacao
    FOR EACH ROW
EXECUTE FUNCTION validar_saldo_transacao();

CREATE TRIGGER trg_validar_quantidade
    BEFORE INSERT ON movimentacao
    FOR EACH ROW
EXECUTE FUNCTION validar_quantidade_movimentacao();
