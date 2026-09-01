package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import java.sql.Connection
import java.sql.SQLException

open class Movimentacao(
    val id: Long? = null,

    val produto: CaixaDeAgua,
    val quantidade: Int,
    val tipo: TipoMovimentacao
){
    fun verificarQuantidade(): Boolean{
        return !(tipo == TipoMovimentacao.SAIDA && quantidade > produto.quantidade)
    }

    fun movimentar(){
        var c: Connection? = null
        if(!verificarQuantidade()){
            println("Movimentação Cancelada: quantidade de saída menor do que a quantidade de produtos em estoque")
            return
        }
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
            stmtProduto.executeUpdate()

            c.commit()

            println("Produto movimentado: quantidade ${produto.quantidade} -> $quantidade")
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