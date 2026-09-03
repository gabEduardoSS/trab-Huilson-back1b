package com.example.huilsonbackendprojeto1b.utils

import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.repositorio.JPACaixa
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.service.ClienteService
import java.math.BigDecimal
import java.sql.Connection

fun main(){
    val con: Connection? = JPAConexao.conectar()

    val clientes = ClienteService().listarClientes()
    clientes.forEach { cliente ->
        println(cliente.valores())
    }

    val cliente: Cliente = clientes.first { it.id == readln().toLong() }

    println(JPACaixa.consultarSaldo(con))
    val transacao = Transacao(
        valor = BigDecimal("1"),
        pessoa = cliente,
        tipoTransacao = TipoTransacao.ENTRADA,
        descricao = "teste2"
    )
    println(transacao.transacao())
    println(JPACaixa.consultarSaldo(con))
}