package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.service.CompraService
import com.example.huilsonbackendprojeto1b.service.FuncionarioService
import com.example.huilsonbackendprojeto1b.service.ProdutoService

class CompraHandler(
    private val compraService: CompraService,
    private val produtoService: ProdutoService,
    private val funcionarioService: FuncionarioService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Realizar Compra" to { realizarCompra() }
    )

    private fun realizarCompra() {
        val produtos = produtoService.listarProdutos()
        val requisitor = funcionarioService.consultarPorCargo(Cargo.FINANCEIRO)


    }
}