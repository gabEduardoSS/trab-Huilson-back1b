package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import java.sql.Connection
import java.sql.SQLException

class Movimentacao(
    val id: Long? = null,

    val produto: CaixaDeAgua,
    val quantidade: Int,
    val tipo: TipoMovimentacao
){
    fun movimentar(){
        var c: Connection? = null
        try{
            c = JPAConexao().conectar()
            c!!.autoCommit = false

            val sqlMovimentacao = "INSERT INTO movimentacao (id_caixa, id_produto, quantidade, tipo) VALUES (1, ?, ?, ?)"

            val stmtMovimentacao = c.prepareStatement(sqlMovimentacao)
            stmtMovimentacao.setLong(1, produto.id!!)
            stmtMovimentacao.setInt(2, quantidade)
            stmtMovimentacao.setString(3, tipo.name)
            stmtMovimentacao.executeUpdate()

            val sqlProduto = "UPDATE caixa_de_agua SET quantidade = ? WHERE id = ?"

            val stmtProduto = c.prepareStatement(sqlProduto)
            stmtProduto.setInt(1, quantidade)
            stmtProduto.setLong(2, produto.id!!)

            c.commit()
        } catch(e: SQLException){
            println("Erro: ${e.printStackTrace()}")
            try {
                c?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            c?.close()
        }
    }
}