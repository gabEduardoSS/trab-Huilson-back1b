package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.produto.Movimentacao
import java.sql.Connection
import java.sql.SQLException

object JPAMovimentacao {
    fun criarMovimentacao(movimentacao: Movimentacao, con: Connection? = null): String? {
        try {
            val sqlInsert = "INSERT INTO movimentacao(id_transacao, id_produto, quantidade, tipo, descricao, status) VALUES(?, ?, ?, ?, ?, 'PENDENTE') RETURNING status"
            val stmtInsert = con!!.prepareStatement(sqlInsert)
            stmtInsert.setObject(1, movimentacao.transacao?.id)
            stmtInsert.setLong(2, movimentacao.produto.id!!)
            stmtInsert.setInt(3, movimentacao.quantidade)
            stmtInsert.setString(4, movimentacao.tipo.name)
            stmtInsert.setString(5, movimentacao.descricao)
            val retorno = stmtInsert.executeQuery()
            retorno.next()
            val status = retorno.getString("status")
            stmtInsert.close()

            return status
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        }
        return null
    }
}