package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.service.VendaService

class VendaHandler(
    private val vendaService: VendaService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Realizar Venda" to { realizarVenda() },
        "Consultar" to { consultarVendas() },

        )

    fun realizarVenda() {}

    fun consultarVendas() {}
}