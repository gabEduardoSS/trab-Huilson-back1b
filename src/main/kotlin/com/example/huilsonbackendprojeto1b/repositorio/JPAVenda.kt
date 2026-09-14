package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.financeiro.Compra
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.financeiro.Venda
import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import java.sql.Connection
import java.sql.SQLException

object JPAVenda {
    fun criarVenda(venda: Venda, con: Connection? = null): Venda? {
        var conexaoInterna: Connection? = null
        try{
            conexaoInterna = con ?: JPAConexao.conectar()
            conexaoInterna!!.autoCommit = false

            val transacao = Transacao(
                valor = venda.valorTotal - venda.valorDesconto,
                pessoa = venda.cliente,
                tipoTransacao = TipoTransacao.ENTRADA
            )
            JPATransacao.criarTransacao(transacao, conexaoInterna)

            if(transacao.status != "CONCLUIDA"){
                println("Erro na transação, cancelando venda")
                conexaoInterna.rollback()
                return null
            }

            val sql = "INSERT INTO venda(id_transacao, id_funcionario, id_cliente, valor_total, descricao, status) VALUES(?, ?, ?, ?, ?, ?) RETURNING id, status, data"
            val stmt = conexaoInterna.prepareStatement(sql)
            stmt.setLong(1, transacao.id!!)
            stmt.setLong(2, venda.vendedor.id!!)
            stmt.setLong(3, venda.cliente.id!!)
            stmt.setBigDecimal(4, venda.valorTotal)
            stmt.setString(5, venda.descricao)
            stmt.setString(6, transacao.status)

            val rs = stmt.executeQuery()
            rs.next()
            venda.id = rs.getLong("id")

            venda.itensVenda.forEach { item ->
                val movimentacao = Movimentacao(
                    produto = item.produto,
                    quantidade = item.quantidade,
                    tipo = TipoMovimentacao.SAIDA
                )
                JPAMovimentacao.criarMovimentacao(movimentacao, conexaoInterna)

                if(movimentacao.status != "CONCLUIDA"){
                    println("Item em estoque menor do que a quantidade vendida, cancelando venda")
                    conexaoInterna.rollback()
                    return null
                }

                val sqlItem = "INSERT INTO item_venda(id_venda, id_produto, id_movimentacao, preco_unitario, quantidade, preco_total_item) VALUES(?, ?, ?, ?, ?, ?) RETURNING id"
                val stmtItem = conexaoInterna.prepareStatement(sqlItem)
                stmtItem.setLong(1, venda.id!!)
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
            venda.status = rs.getString("status")
            venda.data = rs.getTimestamp("data").toLocalDateTime()
            stmt.close()

            conexaoInterna.commit()
            return venda

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

    fun consultarVendas(con: Connection? = null): List<Venda>?{
        var conexaoInterna: Connection? = null
        try{
            conexaoInterna = con ?: JPAConexao.conectar()

        } catch(e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            if(con == null){
                conexaoInterna?.close()
            }
        }
        return null
    }
}
