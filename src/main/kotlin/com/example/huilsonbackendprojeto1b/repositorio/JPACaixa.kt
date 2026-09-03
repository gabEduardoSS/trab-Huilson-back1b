package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException

object JPACaixa{
    fun consultarSaldo(con: Connection? = null): BigDecimal? {
        var conexaoInterna: Connection? = null
        try{
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
        } catch(e: SQLException){
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
            return null
        } finally{
            if(con == null){
                conexaoInterna?.close()
            }
        }
    }

    fun adicionarSaldo(valor: BigDecimal, con: Connection? = null){
        var conexaoInterna: Connection? = null
        try{
            conexaoInterna = con ?: JPAConexao.conectar()

            val saldo = consultarSaldo(conexaoInterna)
            if(saldo == null){
                println("Erro ao consultar o saldo, valor não foi adicionado")
                return
            } else if(valor < BigDecimal.ZERO && valor > saldo){
                println("Saldo de saída maior que o saldo atual, cancelando operação")
                return
            }

            val sql = "UPDATE caixa SET saldo = saldo + ? WHERE id = 1"
            val stmt = conexaoInterna!!.prepareStatement(sql)
            stmt.setBigDecimal(1, valor)
            stmt.executeUpdate()
            println("Valor de ${formatacaoDinheiro(valor)} adicionado. Saldo disponível em caixa: ${formatacaoDinheiro(saldo+valor)}")
            stmt.close()

        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            if (con == null) {
                conexaoInterna?.close()
            }
        }
    }
}