package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.financeiro.Compra
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import java.sql.Connection
import java.sql.SQLException

object JPACompra {
    fun criarCompra(compra: Compra, con: Connection? = null): Compra? {
        var conexaoInterna: Connection? = null
        try{
            conexaoInterna = con ?: JPAConexao.conectar()
            conexaoInterna!!.autoCommit = false

            val transacao = Transacao(
                valor = compra.valorTotal - compra.valorDesconto,
                pessoa = compra.requisitor,
                tipoTransacao = TipoTransacao.SAIDA
            )
            JPATransacao.criarTransacao(transacao, conexaoInterna)

            if(transacao.status != "CONCLUIDA"){
                print("Erro na transação, cancelando compra: ")
                if(transacao.status?.uppercase()?.contains("SALDO") == true ){
                    println("Saldo insuficiente")
                }
                conexaoInterna.rollback()
                return null
            }

            val sql = "INSERT INTO compra(id_transacao, id_funcionario, valor_total, descricao, status) VALUES(?, ?, ?, ?, ?) RETURNING id, status, data"
            val stmt = conexaoInterna.prepareStatement(sql)
            stmt.setLong(1, transacao.id!!)
            stmt.setLong(2, compra.requisitor.id!!)
            stmt.setBigDecimal(3, compra.valorTotal)
            stmt.setString(4, compra.descricao)
            stmt.setString(5, transacao.status)

            val rs = stmt.executeQuery()
            rs.next()
            compra.id = rs.getLong("id")
            compra.status = rs.getString("status")
            compra.data = rs.getTimestamp("data").toLocalDateTime()
            stmt.close()

            compra.itensCompra.forEach { item ->
                val movimentacao = Movimentacao(
                    produto = item.produto,
                    quantidade = item.quantidade,
                    tipo = TipoMovimentacao.ENTRADA
                )
                JPAMovimentacao.criarMovimentacao(movimentacao, conexaoInterna)

                val sqlItem = "INSERT INTO item_compra(id_compra, id_produto, id_movimentacao, preco_unitario, quantidade, preco_total_item) VALUES(?, ?, ?, ?, ?, ?) RETURNING id"
                val stmtItem = conexaoInterna.prepareStatement(sqlItem)
                stmtItem.setLong(1, compra.id!!)
                stmtItem.setLong(2, movimentacao.produto.id!!)
                stmtItem.setLong(3, movimentacao.id!!)
                stmtItem.setBigDecimal(4, movimentacao.produto.preco)
                stmtItem.setInt(5, movimentacao.quantidade)
                stmtItem.setBigDecimal(6, (movimentacao.quantidade).toBigDecimal() * movimentacao.produto.preco)

                val rsItem = stmtItem.executeQuery()
                rsItem.next()
                item.setValues(rsItem.getLong("id"), movimentacao)
                stmtItem.close()
            }

            conexaoInterna.commit()
            return compra

        } catch(e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
            try {
                conexaoInterna?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            if(con == null){
                conexaoInterna?.close()
            }
        }
        return null
    }
}
