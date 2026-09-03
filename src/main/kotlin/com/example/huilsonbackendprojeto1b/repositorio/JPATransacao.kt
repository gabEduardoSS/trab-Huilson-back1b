package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import java.sql.Connection
import java.sql.SQLException

object JPATransacao {
    fun criarTransacao(transacao: Transacao, con: Connection? = null): List<String>? {
        try {
            val sqlInsert = "INSERT INTO transacao(valor, id_caixa, id_pessoa, tipo, descricao, status) VALUES(?, 1, ?, ?, ?, 'PENDENTE') RETURNING status"
            val stmtInsert = con!!.prepareStatement(sqlInsert)
            stmtInsert.setBigDecimal(1, transacao.valor)
            stmtInsert.setLong(2, transacao.pessoa.id!!)
            stmtInsert.setString(3, transacao.tipoTransacao.toString())
            stmtInsert.setString(4, transacao.descricao)

            val rs = stmtInsert.executeQuery()
            rs.next()

            println(rs)
            val retorno: List<String> = listOf(rs.getString("status"))
            stmtInsert.close()

            return retorno
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        }
        return null
    }
}