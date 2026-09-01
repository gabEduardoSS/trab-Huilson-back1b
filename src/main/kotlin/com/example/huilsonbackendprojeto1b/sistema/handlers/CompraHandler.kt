package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.service.CompraService

class CompraHandler(
    private val compraService: CompraService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(

    )
}