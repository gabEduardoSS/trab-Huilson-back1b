package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import com.example.huilsonbackendprojeto1b.repositorio.JPACaixa
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.repositorio.JPATransacao
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
    val status: String? = null,

    val dataMovimentacao : LocalDateTime? = null,
){
    fun transacao(): String?{
        var con: Connection? = null
        try{
            con = JPAConexao.conectar()

            return JPATransacao.criarTransacao(this, con)
        } catch(e: SQLException){
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            con?.close()
        }
        return null
    }
}