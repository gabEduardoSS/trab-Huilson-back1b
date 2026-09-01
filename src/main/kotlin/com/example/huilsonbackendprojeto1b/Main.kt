package com.example.huilsonbackendprojeto1b

import com.example.huilsonbackendprojeto1b.enumeradores.OpcoesMenu
import com.example.huilsonbackendprojeto1b.service.ClienteService
import com.example.huilsonbackendprojeto1b.service.CompraService
import com.example.huilsonbackendprojeto1b.service.FuncionarioService
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import com.example.huilsonbackendprojeto1b.service.VendaService
import com.example.huilsonbackendprojeto1b.sistema.handlers.ClienteHandler
import com.example.huilsonbackendprojeto1b.sistema.handlers.CompraHandler
import com.example.huilsonbackendprojeto1b.sistema.handlers.FuncionarioHandler
import com.example.huilsonbackendprojeto1b.sistema.handlers.ProdutoHandler
import com.example.huilsonbackendprojeto1b.sistema.handlers.VendaHandler
import com.example.huilsonbackendprojeto1b.sistema.mainMenu

fun main(args: Array<String>) {
    val clienteService = ClienteService()
    val funcionarioService = FuncionarioService()
    val produtoService = ProdutoService()
    val vendaService = VendaService()
    val compraService = CompraService()

    val produtoHandler = ProdutoHandler(produtoService)
    val clienteHandler = ClienteHandler(clienteService)
    val funcionarioHandler = FuncionarioHandler(funcionarioService)
    val vendaHandler = VendaHandler(vendaService)
    val compraHandler = CompraHandler(compraService)

    val handlers = mapOf(
        OpcoesMenu.PRODUTO to produtoHandler,
        OpcoesMenu.CLIENTE to clienteHandler,
        OpcoesMenu.FUNCIONARIO to funcionarioHandler,
        OpcoesMenu.VENDA to vendaHandler,
        OpcoesMenu.COMPRA to compraHandler,
    )

    mainMenu(handlers)
}
