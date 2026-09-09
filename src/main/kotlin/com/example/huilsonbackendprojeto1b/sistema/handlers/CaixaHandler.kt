package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.repositorio.JPACaixa
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro

class CaixaHandler: OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Consultar Saldo" to { consultarSaldo() }
    )

    fun consultarSaldo() {
        val saldo = JPACaixa.consultarSaldo()
        if(saldo != null) {
            println("Saldo: ${formatacaoDinheiro(saldo)}")
        } else{
            println("Erro ao consultar saldo")
        }
    }
}
