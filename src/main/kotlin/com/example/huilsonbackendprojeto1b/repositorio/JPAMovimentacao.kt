package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.produto.Movimentacao
import java.sql.Connection
import java.sql.SQLException

class JPAMovimentacao(
    var c: Connection? = null
) {
    fun movimentar(movimentacao: Movimentacao) {
        try{
            c = JPAConexao().conectar()
            c!!.autoCommit = false
        } catch(e: SQLException){

        } finally {
            c?.close()
        }
    }
}