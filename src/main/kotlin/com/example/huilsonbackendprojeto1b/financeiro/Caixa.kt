package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal

class Caixa(
    var saldo : BigDecimal
) {
    fun consultarSaldo(){
        println("Saldo: ${formatacaoDinheiro(saldo)}")
    }

    fun consultarMovimentacoes() {
        println("Movimentações: beta")
    }
}