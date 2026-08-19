package com.example.huilsonbackendprojeto1b.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun formatacaoDinheiro(valor: BigDecimal): String {
    return valor.setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")
}

