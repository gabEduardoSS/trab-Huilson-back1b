package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.service.CaixaService
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro

class CaixaHandler(
    private val caixaService: CaixaService
): OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Consultar Saldo" to { consultarSaldo() }
    )

    fun consultarSaldo() {
        val saldo = caixaService.consultarSaldo()
        if(saldo != null) {
            println("Saldo: ${formatacaoDinheiro(saldo)}")
        } else{
            println("Erro ao consultar saldo")
        }
    }
}
