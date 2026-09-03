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

    val dataMovimentacao : LocalDateTime? = null,
){
    fun transacao(){
        var con: Connection? = null
        try{
            con = JPAConexao.conectar()

            JPATransacao.criarTransacao(this, con)
            JPACaixa.adicionarSaldo(valor * tipoTransacao.valor, con)

        } catch(e: SQLException){
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            con?.close()
        }
    }
}