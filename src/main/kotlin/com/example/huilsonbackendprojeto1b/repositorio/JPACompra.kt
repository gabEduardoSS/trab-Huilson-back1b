package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.financeiro.Compra
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import java.sql.Connection
import java.sql.SQLException

object JPACompra {
    fun criarCompra(compra: Compra, con: Connection? = null): Map<String, Any>? {
        var conexaoInterna: Connection? = null
        try{
            conexaoInterna = con ?: JPAConexao.conectar()

            val transacao = Transacao(
                valor = compra.valorTotal - compra.valorDesconto,
                pessoa = compra.requisitor,
                tipoTransacao = TipoTransacao.SAIDA
            )
            transacao.transacao()

            if(transacao.status != "CONCLUIDA"){
                print("Erro na transação, cancelando compra: ")
                if(transacao.status?.uppercase()?.contains("SALDO") == true ){
                    println("Saldo insuficiente")
                }
                return null
            }

            val sql = "INSERT INTO compra(id_transacao, id_funcionario, valor_total, descricao, status) VALUES(?, ?, ?, ?, ?) RETURNING id, status, data"
            val stmt = con!!.prepareStatement(sql)
            stmt.setLong(1, transacao.id!!)
            stmt.setLong(2, compra.requisitor.id!!)
            stmt.setBigDecimal(3, compra.valorTotal)
            stmt.setString(4, compra.descricao)
            stmt.setString(5, transacao.status)

            val rs = stmt.executeQuery()
            rs.next()
            val idCompra = rs.getInt("id")
            val retorno: Map<String, Any> = mapOf(
                "id" to rs.getInt("id"),
                "status" to rs.getString("status"),
                "data" to rs.getTimestamp("data").toLocalDateTime())

            stmt.close()

            compra.itensCompra.forEach { item ->
                val movimentacao = Movimentacao(
                    produto = item.produto,
                    quantidade = item.quantidade,
                    tipo = TipoMovimentacao.ENTRADA
                )
                movimentacao.movimentacao()

                val sql = "INSERT INTO item_compra(id_compra, id_produto, id_movimentacao, preco_unitario, quantidade) VALUES(?, ?, ?, ?, ?) RETURNING id"
                val stmt = con.prepareStatement(sql)
                stmt.setInt(1, idCompra)
                stmt.setLong(2, movimentacao.produto.id!!)
                stmt.setLong(3, movimentacao.id!!)
                stmt.setBigDecimal(4, movimentacao.produto.preco)
                stmt.setInt(5, movimentacao.quantidade)

                val rs = stmt.executeQuery()
                rs.next()
                item.setValues(rs.getLong("id"), movimentacao)
                stmt.close()

            }
            return retorno

        } catch(e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            if(con == null){
                conexaoInterna?.close()
            }
        }
        return null
    }

    fun consultarCompras(con: Connection? = null): List<Compra>?{
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