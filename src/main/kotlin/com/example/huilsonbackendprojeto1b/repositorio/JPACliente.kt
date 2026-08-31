package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp

class JPACliente(
    var c: Connection? = null
) {
    fun salvar(cliente: Cliente): Cliente {
        println("Salvando...")
        try {
            c = JPAConexao().conectar()
            c!!.autoCommit = false

            val sqlPessoa = "INSERT INTO pessoa " +
                    "(nome, cpf, email, telefone, cidade, endereco, dt_nasc, tipo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

            val stmtPessoa = c!!.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)
            stmtPessoa.setString(1, cliente.nome)
            stmtPessoa.setString(2, cliente.cpf)
            stmtPessoa.setString(3, cliente.email)
            stmtPessoa.setString(4, cliente.telefone)
            stmtPessoa.setString(5, cliente.cidade)
            stmtPessoa.setString(6, cliente.endereco)
            stmtPessoa.setDate(7, cliente.dtNasc?.let { Date.valueOf(it) })
            stmtPessoa.setString(8, TipoPessoa.CLIENTE.name)
            stmtPessoa.executeUpdate()

            val chaves = stmtPessoa.generatedKeys
            var idGerado = 0L
            if (chaves.next()) {
                idGerado = chaves.getLong(1)
            }
            stmtPessoa.close()

            val sqlCliente = "INSERT INTO cliente (id, dividas_abertas) VALUES (?, ?)"
            val stmtCliente = c!!.prepareStatement(sqlCliente)
            stmtCliente.setLong(1, idGerado)
            stmtCliente.setBoolean(2, cliente.dividasAbertas)
            stmtCliente.executeUpdate()
            stmtCliente.close()

            c!!.commit()
            cliente.id = idGerado
        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
            try {
                c?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            c?.close() // agora garantido mesmo se der erro no meio do caminho
        }
        return cliente
    }

    fun listar(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        try {
            c = JPAConexao().conectar()
            val stmt = c!!.createStatement()

            val sql = "SELECT p.id, p.nome, p.cpf, p.email, p.telefone, p.cidade, p.endereco, " +
                    "p.dt_nasc, p.dt_criacao, cl.dividas_abertas " +
                    "FROM pessoa p JOIN cliente cl ON p.id = cl.id"

            val resultado = stmt.executeQuery(sql)

            while (resultado.next()) {
                val cliente = Cliente(
                    nome = resultado.getString("nome"),
                    cpf = resultado.getString("cpf"),
                    email = resultado.getString("email"),
                    telefone = resultado.getString("telefone"),
                    cidade = resultado.getString("cidade"),
                    endereco = resultado.getString("endereco"),
                    dtNasc = resultado.getDate("dt_nasc")?.toLocalDate() ,
                    dividasAbertas = resultado.getBoolean("dividas_abertas"),
                )
                cliente.id = resultado.getLong("id")
                cliente.dtCriacao = resultado.getTimestamp("dt_criacao").toLocalDateTime()
                clientes.add(cliente)
            }

            stmt.close()
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
        return clientes
    }

    fun editar(clienteAtualizado: Cliente, id: Long): Cliente {
        try {
            c = JPAConexao().conectar()
            c!!.autoCommit = false

            val sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ?, " +
                    "cidade = ?, endereco = ?, dt_nasc = ? WHERE id = ?"
            val stmtPessoa = c!!.prepareStatement(sqlPessoa)
            stmtPessoa.setString(1, clienteAtualizado.nome)
            stmtPessoa.setString(2, clienteAtualizado.cpf)
            stmtPessoa.setString(3, clienteAtualizado.email)
            stmtPessoa.setString(4, clienteAtualizado.telefone)
            stmtPessoa.setString(5, clienteAtualizado.cidade)
            stmtPessoa.setString(6, clienteAtualizado.endereco)
            stmtPessoa.setDate(7, clienteAtualizado.dtNasc?.let { Date.valueOf(it) })
            stmtPessoa.setLong(8, id)
            stmtPessoa.executeUpdate()
            stmtPessoa.close()

            val sqlCliente = "UPDATE cliente SET dividas_abertas = ? WHERE id = ?"
            val stmtCliente = c!!.prepareStatement(sqlCliente)
            stmtCliente.setBoolean(1, clienteAtualizado.dividasAbertas)
            stmtCliente.setLong(2, id)
            stmtCliente.executeUpdate()
            stmtCliente.close()

            c!!.commit()
            clienteAtualizado.id = id
        } catch (e: SQLException) {
            println(e.printStackTrace())
            try {
                c?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            c?.close()
        }
        return clienteAtualizado
    }
}
