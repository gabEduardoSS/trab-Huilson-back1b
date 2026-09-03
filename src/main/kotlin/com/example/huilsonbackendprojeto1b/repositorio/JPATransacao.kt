package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import java.sql.Connection
import java.sql.SQLException

object JPATransacao {
    fun criarTransacao(transacao: Transacao, con: Connection? = null) {
        try {
            val sqlInsert = "INSERT INTO transacao(valor, id_caixa, id_pessoa, tipo, descricao) VALUES(?, 1, ?, ?, ?)"
            val stmtInsert = con!!.prepareStatement(sqlInsert)
            stmtInsert.setBigDecimal(1, transacao.valor)
            stmtInsert.setLong(2, transacao.pessoa.id!!)
            stmtInsert.setString(3, transacao.tipoTransacao.toString())
            stmtInsert.setString(4, transacao.descricao)
            stmtInsert.executeUpdate()
            stmtInsert.close()
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        }
    }
}