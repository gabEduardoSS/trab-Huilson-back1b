package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.repositorio.JPAMovimentacao
import java.sql.Connection
import java.sql.SQLException
import java.time.LocalDateTime

open class Movimentacao(
    val id: Long? = null,

    val produto: CaixaDeAgua,
    val quantidade: Int,
    val descricao: String? = null,
    val tipo: TipoMovimentacao
){
    var quantidadeAnterior: Int? = null
    var quantidadePosterior: Int? = null
    var status: String? = null
    var dataMovimentacao: LocalDateTime? = null

    fun movimentacao(){
        var con: Connection? = null
        try{
            con = JPAConexao.conectar()

            val retorno = JPAMovimentacao.criarMovimentacao(this, con)

            quantidadeAnterior = retorno?.getValue("quantidade_anterior") as Int?
            quantidadePosterior = retorno?.getValue("quantidade_posterior") as Int?
            status = retorno?.getValue("status") as String?
            dataMovimentacao = retorno?.getValue("data") as LocalDateTime?
        } catch(e: SQLException){
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            con?.close()
        }
    }

    fun valores(){
        print("""
            Quantidade: ${quantidade},
            ID do Produto: ${produto.id},
            Tipo: ${tipo.name},
            Descricao: ${descricao},
            Quantidade Anterior: ${quantidadeAnterior},
            Quantidade Posterior: ${quantidadePosterior},
            Status: ${status},
            Data: $dataMovimentacao
        """.trimIndent())
    }
}