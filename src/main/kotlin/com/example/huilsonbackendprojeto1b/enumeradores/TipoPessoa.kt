package com.example.huilsonbackendprojeto1b.enumeradores

import java.math.BigDecimal

enum class TipoPessoa(val multiplicador: BigDecimal) {
    CLIENTE("1".toBigDecimal()),
    FUNCIONARIO("-1".toBigDecimal()),
}