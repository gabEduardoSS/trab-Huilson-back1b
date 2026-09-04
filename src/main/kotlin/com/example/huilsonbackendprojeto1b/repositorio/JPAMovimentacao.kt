package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.produto.Movimentacao
import java.sql.Connection
import java.sql.SQLException

object JPAMovimentacao {
    fun criarMovimentacao(movimentacao: Movimentacao, con: Connection? = null): Map<String, Any>? {
        try {
            val sqlInsert = "INSERT INTO movimentacao(id_produto, quantidade, tipo, descricao, status) VALUES(?, ?, ?, ?, 'PENDENTE') RETURNING status, data, quantidade_anterior, quantidade_posterior"
            val stmtInsert = con!!.prepareStatement(sqlInsert)
            stmtInsert.setLong(1, movimentacao.produto.id!!)
            stmtInsert.setInt(2, movimentacao.quantidade)
            stmtInsert.setString(3, movimentacao.tipo.name)
            stmtInsert.setString(4, movimentacao.descricao)

            val rs = stmtInsert.executeQuery()
            rs.next()

            val retorno: Map<String, Any> = mapOf(
                "quantidade_anterior" to rs.getInt("quantidade_anterior"),
                "quantidade_posterior" to rs.getInt("quantidade_posterior"),
                "status" to rs.getString("status"),
                "data" to rs.getTimestamp("data").toLocalDateTime())
            stmtInsert.close()

            return retorno
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        }
        return null
    }
}