package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.sql.Timestamp

class JPAFuncionario(
    var c: Connection? = null
) {

    fun salvar(funcionario: Funcionario): Funcionario {
        println("Salvando...")
        try {
            c = JPAConexao().conectar()
            c!!.autoCommit = false

            val sqlPessoa = "INSERT INTO pessoa " +
                    "(nome, cpf, email, telefone, cidade, endereco, dt_nasc, tipo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

            val stmtPessoa = c!!.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)
            stmtPessoa.setString(1, funcionario.nome)
            stmtPessoa.setString(2, funcionario.cpf)
            stmtPessoa.setString(3, funcionario.email)
            stmtPessoa.setString(4, funcionario.telefone)
            stmtPessoa.setString(5, funcionario.cidade)
            stmtPessoa.setString(6, funcionario.endereco)
            stmtPessoa.setDate(7, funcionario.dtNasc?.let { Date.valueOf(it) })
            stmtPessoa.setString(8, TipoPessoa.FUNCIONARIO.name)
            stmtPessoa.executeUpdate()

            val chaves = stmtPessoa.generatedKeys
            var idGerado = 0L
            if (chaves.next()) {
                idGerado = chaves.getLong(1)
            }
            stmtPessoa.close()

            val sqlFuncionario = "INSERT INTO funcionario (id, salario, turno, cargo, status) " +
                    "VALUES (?, ?, ?, ?, ?)"
            val stmtFuncionario = c!!.prepareStatement(sqlFuncionario)
            stmtFuncionario.setLong(1, idGerado)
            stmtFuncionario.setBigDecimal(2, funcionario.salario)
            stmtFuncionario.setString(3, funcionario.turno.name)
            stmtFuncionario.setString(4, funcionario.cargo.name)
            stmtFuncionario.setString(5, funcionario.status)
            stmtFuncionario.executeUpdate()
            stmtFuncionario.close()

            c!!.commit()
            funcionario.id = idGerado
        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
            try {
                c?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            c?.close()
        }
        return funcionario
    }

    fun listar(): List<Funcionario> {
        return listarComFiltroDeStatus(null)
    }

    fun consultarPorStatus(status: String): List<Funcionario> {
        return listarComFiltroDeStatus(status)
    }

    private fun listarComFiltroDeStatus(status: String?): List<Funcionario> {
        val funcionarios = mutableListOf<Funcionario>()
        try {
            c = JPAConexao().conectar()

            var sql = "SELECT p.id, p.nome, p.cpf, p.email, p.telefone, p.cidade, p.endereco, " +
                    "p.dt_nasc, p.dt_criacao, f.salario, f.turno, f.cargo, f.status " +
                    "FROM pessoa p JOIN funcionario f ON p.id = f.id"
            if (status != null) {
                sql += " WHERE f.status = ?"
            }

            val stmt = c!!.prepareStatement(sql)
            if (status != null) {
                stmt.setString(1, status)
            }

            val resultado = stmt.executeQuery()

            while (resultado.next()) {
                val funcionario = Funcionario(
                    nome = resultado.getString("nome"),
                    cpf = resultado.getString("cpf"),
                    email = resultado.getString("email"),
                    telefone = resultado.getString("telefone"),
                    cidade = resultado.getString("cidade"),
                    endereco = resultado.getString("endereco"),
                    dtNasc = resultado.getDate("dt_nasc").toLocalDate(),
                    salario = resultado.getBigDecimal("salario"),
                    turno = Turno.valueOf(resultado.getString("turno")),
                    cargo = Cargo.valueOf(resultado.getString("cargo")),
                    status = resultado.getString("status"),
                )
                funcionario.id = resultado.getLong("id")
                funcionario.dtCriacao = resultado.getTimestamp("dt_criacao").toLocalDateTime()
                funcionarios.add(funcionario)
            }

            stmt.close()
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
        return funcionarios
    }

    fun consultarPorCargo(cargo: Cargo): List<Funcionario> {
        val funcionarios = mutableListOf<Funcionario>()
        try {
            c = JPAConexao().conectar()

            var sql = "SELECT p.id, p.nome, p.cpf, p.email, p.telefone, p.cidade, p.endereco, " +
                    "p.dt_nasc, p.dt_criacao, f.salario, f.turno, f.cargo, f.status " +
                    "FROM pessoa p JOIN funcionario f ON p.id = f.id WHERE f.cargo = ?"

            val stmt = c!!.prepareStatement(sql)

            stmt.setString(1, cargo.toString())


            val resultado = stmt.executeQuery()

            while (resultado.next()) {
                val funcionario = Funcionario(
                    nome = resultado.getString("nome"),
                    cpf = resultado.getString("cpf"),
                    email = resultado.getString("email"),
                    telefone = resultado.getString("telefone"),
                    cidade = resultado.getString("cidade"),
                    endereco = resultado.getString("endereco"),
                    dtNasc = resultado.getDate("dt_nasc").toLocalDate(),
                    salario = resultado.getBigDecimal("salario"),
                    turno = Turno.valueOf(resultado.getString("turno")),
                    cargo = Cargo.valueOf(resultado.getString("cargo")),
                    status = resultado.getString("status"),
                )
                funcionario.id = resultado.getLong("id")
                funcionario.dtCriacao = resultado.getTimestamp("dt_criacao").toLocalDateTime()
                funcionarios.add(funcionario)
            }

            stmt.close()
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
        return funcionarios
    }

    fun alterarStatus(id: Long, status: String) {
        try {
            c = JPAConexao().conectar()
            val sql = "UPDATE funcionario SET status = ? WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, status)
            stmt.setLong(2, id)
            stmt.executeUpdate()
            stmt.close()
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
    }

    fun editar(funcionarioAtualizado: Funcionario, id: Long): Funcionario {
        try {
            c = JPAConexao().conectar()
            c!!.autoCommit = false

            val sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ?, email = ?, telefone = ?, " +
                    "cidade = ?, endereco = ?, dt_nasc = ? WHERE id = ?"
            val stmtPessoa = c!!.prepareStatement(sqlPessoa)
            stmtPessoa.setString(1, funcionarioAtualizado.nome)
            stmtPessoa.setString(2, funcionarioAtualizado.cpf)
            stmtPessoa.setString(3, funcionarioAtualizado.email)
            stmtPessoa.setString(4, funcionarioAtualizado.telefone)
            stmtPessoa.setString(5, funcionarioAtualizado.cidade)
            stmtPessoa.setString(6, funcionarioAtualizado.endereco)
            stmtPessoa.setDate(7, funcionarioAtualizado.dtNasc?.let { Date.valueOf(it) })
            stmtPessoa.setLong(8, id)
            stmtPessoa.executeUpdate()
            stmtPessoa.close()

            val sqlFuncionario = "UPDATE funcionario SET salario = ?, turno = ?, cargo = ? WHERE id = ?"
            val stmtFuncionario = c!!.prepareStatement(sqlFuncionario)
            stmtFuncionario.setBigDecimal(1, funcionarioAtualizado.salario)
            stmtFuncionario.setString(2, funcionarioAtualizado.turno.name)
            stmtFuncionario.setString(3, funcionarioAtualizado.cargo.name)
            stmtFuncionario.setLong(4, id)
            stmtFuncionario.executeUpdate()
            stmtFuncionario.close()

            c!!.commit()
            funcionarioAtualizado.id = id
        } catch (e: SQLException) {
            println(e.printStackTrace())
            try {
                c?.rollback()
            } catch (ignored: SQLException) {
            }
        } finally {
            c?.close()
        }
        return funcionarioAtualizado
    }
}
