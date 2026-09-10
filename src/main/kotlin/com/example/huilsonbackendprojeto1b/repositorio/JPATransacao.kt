package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import java.sql.Connection
import java.sql.SQLException

object JPATransacao {
    /**
     * Insere a transação usando a conexão recebida (não abre nem fecha
     * conexão própria) para que quem estiver orquestrando a operação
     * (JPACompra/JPAVenda) consiga manter tudo na mesma transação de banco.
     * Em caso de erro, apenas loga e devolve a transação sem alterações
     * (status continua null), quem chama decide o que fazer.
     */
    fun criarTransacao(transacao: Transacao, con: Connection): Transacao {
        try {
            val sqlInsert = "INSERT INTO transacao(valor, id_caixa, id_pessoa, tipo, status) VALUES(?, 1, ?, ?, 'PENDENTE') RETURNING id, status, data, saldo_anterior, saldo_posterior"
            val stmtInsert = con.prepareStatement(sqlInsert)
            stmtInsert.setBigDecimal(1, transacao.valor)
            stmtInsert.setLong(2, transacao.pessoa.id!!)
            stmtInsert.setString(3, transacao.tipoTransacao.name)

            val rs = stmtInsert.executeQuery()
            rs.next()

            transacao.id = rs.getLong("id")
            transacao.saldoAnterior = rs.getBigDecimal("saldo_anterior")
            transacao.saldoPosterior = rs.getBigDecimal("saldo_posterior")
            transacao.status = rs.getString("status")
            transacao.dataMovimentacao = rs.getTimestamp("data").toLocalDateTime()
            stmtInsert.close()
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}, ${e.message}")
        }
        return transacao
    }
}
