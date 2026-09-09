package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException

object JPACaixa {
    fun consultarSaldo(con: Connection? = null): BigDecimal? {
        var conexaoInterna: Connection? = null
        try {
            conexaoInterna = con ?: JPAConexao.conectar()

            val stmt = conexaoInterna!!.createStatement()
            val sql = "SELECT saldo FROM caixa WHERE id = 1"
            val resultado = stmt.executeQuery(sql)

            val saldo: BigDecimal
            if (resultado.next()) {
                saldo = resultado.getBigDecimal(1)
            } else {
                throw RuntimeException("Nenhum registro encontrado")
            }

            resultado.close()
            stmt.close()
            return saldo
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
            return null
        } finally {
            if (con == null) {
                conexaoInterna?.close()
            }
        }
    }
}
