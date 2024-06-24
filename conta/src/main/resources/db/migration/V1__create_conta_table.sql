CREATE TABLE conta (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    data_vencimento timestamp NOT NULL,
    data_pagamento  timestamp NULL,
    situacao VARCHAR(50) NOT NULL
);