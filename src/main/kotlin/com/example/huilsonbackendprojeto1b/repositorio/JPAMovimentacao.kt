package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import java.sql.Connection
import java.sql.SQLException

object JPAMovimentacao {
    /**
     * Mesma ideia de JPATransacao.criarTransacao: usa a conexão recebida,
     * não abre/fecha conexão própria, e devolve a própria Movimentacao
     * já preenchida com o retorno do banco.
     */
    fun criarMovimentacao(movimentacao: Movimentacao, con: Connection): Movimentacao {
        try {
            val sqlInsert = "INSERT INTO movimentacao(id_produto, quantidade, tipo, descricao, status) VALUES(?, ?, ?, ?, 'PENDENTE') RETURNING id, status, data, quantidade_anterior, quantidade_posterior"
            val stmtInsert = con.prepareStatement(sqlInsert)
            stmtInsert.setLong(1, movimentacao.produto.id!!)
            stmtInsert.setInt(2, movimentacao.quantidade)
            stmtInsert.setString(3, movimentacao.tipo.name)
            stmtInsert.setString(4, movimentacao.descricao)

            val rs = stmtInsert.executeQuery()
            rs.next()

            movimentacao.id = rs.getLong("id")
            movimentacao.quantidadeAnterior = rs.getInt("quantidade_anterior")
            movimentacao.quantidadePosterior = rs.getInt("quantidade_posterior")
            movimentacao.status = rs.getString("status")
            movimentacao.dataMovimentacao = rs.getTimestamp("data").toLocalDateTime()
            stmtInsert.close()
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        }
        return movimentacao
    }
}
