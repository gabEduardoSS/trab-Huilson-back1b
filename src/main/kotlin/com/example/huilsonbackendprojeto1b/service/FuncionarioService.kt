package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import com.example.huilsonbackendprojeto1b.repositorio.JPAFuncionario
import java.sql.SQLException

class FuncionarioService {
    private val jpaFuncionario = JPAFuncionario()

    fun salvarFuncionario(funcionario: Funcionario): Funcionario {
        return jpaFuncionario.salvar(funcionario)
    }

    fun listarFuncionarios(): List<Funcionario> {
        return jpaFuncionario.listar()
    }

    fun consultarPorStatus(status: String): List<Funcionario> {
        return jpaFuncionario.consultarPorStatus(status)
    }

    fun consultarPorCargo(cargo: Cargo): List<Funcionario> {
        return jpaFuncionario.consultarPorCargo(cargo)
    }

    fun alterarStatus(id: Long, status: String) {
        try {
            jpaFuncionario.alterarStatus(id, status)
            println("Funcionário ${if (status == "desativado") status else "reativado"}")
        } catch (e: SQLException) {
            println("ERRO: ${e.printStackTrace()}")
        }
    }

    fun alterarFuncionario(id: Long, funcionarioAtualizado: Funcionario): Funcionario {
        val funcionarios = jpaFuncionario.listar()
        if (funcionarios.none { it.id == id }) {
            throw NoSuchElementException("Funcionário com o ID ${id} não encontrado")
        }
        return jpaFuncionario.editar(funcionarioAtualizado, id)
    }
}
