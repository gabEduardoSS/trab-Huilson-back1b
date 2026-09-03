package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import java.sql.Connection
import java.sql.SQLException

open class Movimentacao(
    val id: Long? = null,

    val transacao: Transacao? = null,
    val produto: CaixaDeAgua,
    val quantidade: Int,
    val descricao: String? = null,
    val tipo: TipoMovimentacao
){
    fun movimentar(): String?{
        if(tipo == TipoMovimentacao.ENTRADA && descricao == null){ return "ERRO: Entrada sem transação associada"}

        var c: Connection? = null
        try{
            c = JPAConexao.conectar()


            println("Produto movimentado: quantidade ${produto.quantidade} -> $quantidade")
        } catch(e: SQLException){
            println("Erro: ${e.printStackTrace()}")
            try {
                c?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            c?.close()
        }
        return null
    }
}