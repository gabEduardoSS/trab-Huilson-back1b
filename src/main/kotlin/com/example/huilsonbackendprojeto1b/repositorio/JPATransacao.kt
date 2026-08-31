package com.example.huilsonbackendprojeto1b.repositorio

import java.sql.Connection

class JPATransacao(
    var c: Connection? = null
) {
    fun criarTransacao(){
        c = JPAConexao().conectar()
    }
}