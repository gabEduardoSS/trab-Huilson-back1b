package com.example.huilsonbackendprojeto1b.enumeradores

import java.math.BigDecimal

enum class TipoTransacao(val valor: BigDecimal) {
    ENTRADA(1.toBigDecimal()),
    SAIDA((-1).toBigDecimal()),
}