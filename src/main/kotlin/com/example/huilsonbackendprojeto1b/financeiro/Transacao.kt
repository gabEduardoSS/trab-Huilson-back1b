package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.repositorio.JPATransacao
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException
import java.time.LocalDateTime

class Transacao(
    val id: Long? = null,

    val valor : BigDecimal,
    val pessoa : Pessoa,
    val tipoTransacao : TipoTransacao,
    val descricao: String?,
){
    var saldoAnterior: BigDecimal? = null
    var saldoPosterior: BigDecimal? = null
    var status: String? = null

    var dataMovimentacao : LocalDateTime? = null

    fun transacao(){
        var con: Connection? = null
        try{
            con = JPAConexao.conectar()

            val retorno = JPATransacao.criarTransacao(this, con)

            saldoAnterior = retorno?.getValue("saldo_anterior") as BigDecimal?
            saldoPosterior = retorno?.getValue("saldo_posterior") as BigDecimal?
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
            Valor: ${formatacaoDinheiro(valor)},
            ID da Pessoa: ${pessoa.id},
            Tipo: ${tipoTransacao.name},
            Descricao: ${descricao},
            Saldo Anterior: ${saldoAnterior},
            Saldo Posterior: ${saldoPosterior},
            Status: ${status},
            Data: $dataMovimentacao
        """.trimIndent())
    }
}