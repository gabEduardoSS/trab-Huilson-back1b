package com.example.huilsonbackendprojeto1b.repositorio

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

open class JPAConexao(
    val user: String = "postgres",
    val senha: String = "postgres",
    val url: String = "jdbc:postgresql://localhost:5432/projetocaixadeagua",
    var c: Connection? = null
) {
    open fun conectar(): Connection? {
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