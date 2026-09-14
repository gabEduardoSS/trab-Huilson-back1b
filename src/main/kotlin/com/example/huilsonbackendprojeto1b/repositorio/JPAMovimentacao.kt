package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formato
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.sql.Connection
import java.sql.SQLException

object JPAMovimentacao {
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

    fun consultarMovimentacao(con: Connection? = null, tipo: TipoMovimentacao? = null): List<Movimentacao>{
        val movimentacoes = mutableListOf<Movimentacao>()
        var conexaoInterna: Connection? = null

        try{
            conexaoInterna = con ?: JPAConexao.conectar()

            val campos = "m.id AS id_movimentacao, m.id_produto, m.quantidade, m.tipo, m.descricao, m.status, m.quantidade_anterior, m.quantidade_posterior, m.data, " +
                    "c.marca, c.modelo, c.dimensao, c.cor, c.material, c.formato, c.status"
            var sql = "SELECT $campos FROM movimentacao m join caixa_de_agua c on m.id_produto = c.id"
            if(tipo != null) sql += " WHERE m.tipo = ?"

            val stmt = conexaoInterna!!.prepareStatement(sql)
            if(tipo != null) stmt.setString(1, tipo.name)
            val rs = stmt.executeQuery()

            while (rs.next()) {
                val dimensaoArray = rs.getArray("dimensao").array as Array<*>
                val dimensao = dimensaoArray.map { (it as Number).toDouble() }.toMutableList()

                val produto = CaixaDeAgua(
                    id = rs.getLong("id_produto"),
                    marca = rs.getString("marca"),
                    modelo = rs.getString("modelo"),
                    dimensao = dimensao,
                    cor = Cor.valueOf(rs.getString("cor")),
                    material = Material.valueOf(rs.getString("material")),
                    formato = Formato.valueOf(rs.getString("formato")),
                    status = rs.getString("status")
                )

                val movimentacao = Movimentacao(
                    id = rs.getLong("id_movimentacao"),
                    produto = produto,
                    quantidade = rs.getInt("quantidade"),
                    descricao = rs.getString("descricao"),
                    tipo = TipoMovimentacao.valueOf(rs.getString("tipo")),
                )
                movimentacao.quantidadeAnterior = rs.getInt("quantidade_anterior")
                movimentacao.quantidadePosterior = rs.getInt("quantidade_posterior")
                movimentacao.status = rs.getString("status")
                movimentacao.dataMovimentacao = rs.getTimestamp("data").toLocalDateTime()

                movimentacoes.add(movimentacao)
            }
            stmt.close()
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}, ${e.message}")
        } finally {
            if(con == null){
                conexaoInterna?.close()
            }
        }
        return movimentacoes
    }
}
