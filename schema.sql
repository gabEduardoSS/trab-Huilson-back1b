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

CREATE TABLE compra(
    id SERIAL PRIMARY KEY,
    id_transacao INT REFERENCES transacao(id) NOT NULL,
    id_funcionario INT REFERENCES funcionario(id) NOT NULL,
    valor_total NUMERIC(19, 4) NOT NULL,
    status VARCHAR(30) NOT NULL,
    descricao VARCHAR(255),
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_compra(
    id SERIAL PRIMARY KEY,
    id_compra INT REFERENCES compra(id) NOT NULL,
    id_produto INT REFERENCES caixa_de_agua(id) NOT NULL,
    id_movimentacao INT REFERENCES movimentacao(id) NOT NULL,
    preco_unitario NUMERIC(19, 4) NOT NULL,
    preco_total_item NUMERIC(19, 4) NOT NULL,
    quantidade INT NOT NULL
);

CREATE TABLE venda(
   id SERIAL PRIMARY KEY,
   id_transacao INT REFERENCES transacao(id) NOT NULL,
   id_funcionario INT REFERENCES funcionario(id) NOT NULL,
   id_cliente INT REFERENCES cliente(id) NOT NULL,
   valor_total NUMERIC(19, 4) NOT NULL,
   status VARCHAR(30) NOT NULL,
   descricao VARCHAR(255),
   data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_venda(
    id SERIAL PRIMARY KEY,
    id_venda INT REFERENCES venda(id) NOT NULL,
    id_produto INT REFERENCES caixa_de_agua(id) NOT NULL,
    id_movimentacao INT REFERENCES movimentacao(id) NOT NULL,
    preco_unitario NUMERIC(19, 4) NOT NULL,
    preco_total_item NUMERIC(19, 4) NOT NULL,
    quantidade INT NOT NULL
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
            NEW.status := 'CANCELADA(saldo)';
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
            NEW.status := 'CANCELADA(quantidade)';
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

-- ---------------------------------------------------------
-- PESSOAS (base para clientes e funcionários)
INSERT INTO pessoa (id, nome, cpf, email, telefone, cidade, endereco, dt_nasc, tipo) VALUES
(1,  'Ana Paula Ferreira',   '123.456.789-01', 'ana.ferreira@email.com',    '(44) 99911-1001', 'Umuarama',    'Rua das Flores, 120',        '1988-03-14', 'CLIENTE'),
(2,  'Carlos Eduardo Souza', '234.567.891-02', 'carlos.souza@email.com',   '(44) 99911-1002', 'Maringá',     'Av. Brasil, 845',            '1975-07-22', 'CLIENTE'),
(3,  'Beatriz Lima Costa',   '345.678.912-03', 'beatriz.lima@email.com',   '(44) 99911-1003', 'Cascavel',    'Rua Paraná, 55',             '1992-11-05', 'CLIENTE'),
(4,  'João Vitor Almeida',   '456.789.123-04', 'joao.almeida@email.com',   '(44) 99911-1004', 'Umuarama',    'Rua Santos Dumont, 310',     '1980-01-30', 'CLIENTE'),
(5,  'Fernanda Rodrigues',   '567.891.234-05', 'fernanda.rodrigues@email.com', '(44) 99911-1005', 'Toledo',  'Av. Independência, 970',     '1995-06-18', 'CLIENTE'),
(6,  'Marcos Antônio Silva', '678.912.345-06', 'marcos.silva@email.com',   '(44) 99911-1006', 'Umuarama',    'Rua Ipê Amarelo, 45',        '1983-09-09', 'CLIENTE'),
(7,  'Juliana Martins',      '789.123.456-07', 'juliana.martins@email.com','(44) 99911-1007', 'Paranavaí',   'Rua Rio de Janeiro, 210',    '1990-12-25', 'CLIENTE'),
(8,  'Ricardo Pereira',      '891.234.567-08', 'ricardo.pereira@email.com','(44) 99911-1008', 'Umuarama',    'Av. Presidente Castelo, 88', '1978-04-11', 'CLIENTE'),
(9,  'Camila Nogueira',      '912.345.678-09', 'camila.nogueira@email.com','(44) 99911-1009', 'Cianorte',    'Rua das Palmeiras, 33',      '1998-02-27', 'CLIENTE'),
(10, 'Eduardo Castro',       '019.283.746-10', 'eduardo.castro@email.com', '(44) 99911-1010', 'Umuarama',    'Rua Curitiba, 402',          '1985-08-19', 'CLIENTE'),
(11, 'Patrícia Gonçalves',   '111.222.333-11', 'patricia.goncalves@empresa.com', '(44) 99922-2001', 'Umuarama', 'Rua dos Trabalhadores, 12',  '1991-05-03', 'FUNCIONARIO'),
(12, 'Rafael Barbosa',       '222.333.444-12', 'rafael.barbosa@empresa.com',     '(44) 99922-2002', 'Umuarama', 'Rua José de Alencar, 77',    '1986-10-14', 'FUNCIONARIO'),
(13, 'Larissa Teixeira',     '333.444.555-13', 'larissa.teixeira@empresa.com',   '(44) 99922-2003', 'Umuarama', 'Av. Duque de Caxias, 500',   '1993-03-21', 'FUNCIONARIO'),
(14, 'Gustavo Henrique Melo','444.555.666-14', 'gustavo.melo@empresa.com',       '(44) 99922-2004', 'Umuarama', 'Rua Tocantins, 158',         '1982-07-08', 'FUNCIONARIO'),
(15, 'Sabrina Duarte',       '555.666.777-15', 'sabrina.duarte@empresa.com',     '(44) 99922-2005', 'Umuarama', 'Rua Minas Gerais, 640',      '1997-01-16', 'FUNCIONARIO');

-- ---------------------------------------------------------
-- CLIENTES
INSERT INTO cliente (id, dividas_abertas) VALUES
(1,  FALSE),
(2,  TRUE),
(3,  FALSE),
(4,  FALSE),
(5,  TRUE),
(6,  FALSE),
(7,  FALSE),
(8,  TRUE),
(9,  FALSE),
(10, FALSE);

-- ---------------------------------------------------------
-- FUNCIONÁRIOS
INSERT INTO funcionario (id, salario, turno, cargo, status) VALUES
(11, 2450.00, 'MATUTINO',   'ATENDIMENTO',     'ativo'),
(12, 3800.50, 'VESPERTINO', 'ADMINISTRATIVO',  'ativo'),
(13, 5200.00, 'MATUTINO',   'FINANCEIRO',      'ativo'),
(14, 2100.00, 'NOTURNO',    'LOGISTICA',       'desativado'),
(15, 3200.75, 'VESPERTINO', 'FINANCEIRO',      'ativo');

-- ---------------------------------------------------------
-- CAIXAS DE ÁGUA (produtos)
INSERT INTO caixa_de_agua (marca, modelo, dimensao, cor, material, formato, fornecedor, preco, status, quantidade, quantidade_minima) VALUES
('Fortlev',   'Fort Plus 1000L',       ARRAY[1.20, 1.20, 1.10], 'AZUL_FORTE', 'POLIETILENO',    'CILINDRICO', 'Distribuidora Água Viva',   450.00,  'ativo',   25, 5),
('Tigre',     'Tigre 500L',            ARRAY[0.95, 0.95, 0.85], 'BRANCO',     'POLIETILENO',    'CILINDRICO', 'Hidro Sul Distribuidora',   280.00,  'ativo',   40, 8),
('Amanco',    'Amanco 2000L',          ARRAY[1.45, 1.45, 1.60], 'AZUL_FRACO', 'POLIETILENO',    'CILINDRICO', 'Distribuidora Água Viva',   890.00,  'ativo',   12, 3),
('Acqualimp', 'Acqua Fibra 5000L',     ARRAY[2.10, 2.10, 1.90], 'CINZA',      'FIBRA_DE_VIDRO', 'CUBICO',     'Fibra Norte Materiais',     2100.00, 'ativo',   4,  2),
('Fortlev',   'Fort Inox 1000L',       ARRAY[1.10, 1.10, 1.15], 'CINZA',      'INOX',           'CILINDRICO', 'Aço Sul Equipamentos',      1350.00, 'ativo',   6,  2),
('Tigre',     'Tigre 310L',            ARRAY[0.75, 0.75, 0.70], 'AZUL_FORTE', 'POLIETILENO',    'CONICO',     'Hidro Sul Distribuidora',   190.00,  'ativo',   55, 10),
('Sansuy',    'Sansuy 3000L',          ARRAY[1.70, 1.70, 1.55], 'BRANCO',     'POLIETILENO',    'CILINDRICO', 'Distribuidora Água Viva',   1180.00, 'ativo',   8,  3),
('Brasilit',  'Brasilit Fibra 10000L', ARRAY[2.90, 2.90, 2.20], 'CINZA',      'FIBRA_DE_VIDRO', 'CUBICO',     'Fibra Norte Materiais',     4200.00, 'ativo',   2,  1),
('Amanco',    'Amanco 750L',           ARRAY[1.00, 1.00, 0.95], 'AZUL_FRACO', 'POLIETILENO',    'CONICO',     'Hidro Sul Distribuidora',   360.00,  'inativo', 0,  5),
('Fortlev',   'Fort Slim 500L',        ARRAY[0.85, 0.85, 1.00], 'BRANCO',     'POLIETILENO',    'CUBICO',     'Distribuidora Água Viva',   310.00,  'ativo',   18, 6);

-- ---------------------------------------------------------
-- Ajusta as sequences para não colidir com os ids inseridos manualmente
-- ---------------------------------------------------------
SELECT setval(pg_get_serial_sequence('pessoa', 'id'), (SELECT MAX(id) FROM pessoa));
SELECT setval(pg_get_serial_sequence('caixa_de_agua', 'id'), (SELECT MAX(id) FROM caixa_de_agua));