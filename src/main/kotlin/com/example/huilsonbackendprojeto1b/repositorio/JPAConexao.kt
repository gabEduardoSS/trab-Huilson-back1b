package com.example.huilsonbackendprojeto1b.repositorio

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object JPAConexao{
    const val user: String = "postgres"
    const val senha: String = "postgres"
    const val url: String = "jdbc:postgresql://localhost:5432/projetocaixadeagua"
    var c: Connection? = null

    fun conectar(): Connection? {
        try {
            Class.forName("org.postgresql.Driver")
            c = DriverManager.getConnection(url, user, senha)
            return c
        } catch (e: SQLException) {
            println("Erro na conexão: ${e.printStackTrace()}")
            return null
        }
    }
}