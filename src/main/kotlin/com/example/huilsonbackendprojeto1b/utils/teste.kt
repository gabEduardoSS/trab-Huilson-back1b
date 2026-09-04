package com.example.huilsonbackendprojeto1b.utils

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.financeiro.Caixa
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.produto.Movimentacao
import com.example.huilsonbackendprojeto1b.repositorio.JPACaixa
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.service.ClienteService
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import com.example.huilsonbackendprojeto1b.sistema.handlers.ClienteHandler
import java.math.BigDecimal
import java.sql.Connection

fun main(){
    val con: Connection? = JPAConexao.conectar()

    val produtos = ProdutoService().listarProdutos()

    produtos.forEach { produto ->
        println(produto.valores())
    }

    val produto: CaixaDeAgua = produtos.first{ it.id == readln().toLong()}

    val movimentacao = Movimentacao(
        produto = produto,
        quantidade = 20,
        descricao = "teste",
        tipo = TipoMovimentacao.SAIDA
    )

    movimentacao.movimentacao()

    movimentacao.valores()

    con?.close()
}