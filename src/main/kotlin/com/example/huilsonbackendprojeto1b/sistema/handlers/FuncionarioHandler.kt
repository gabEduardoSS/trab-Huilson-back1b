package com.example.huilsonbackendprojeto1b.sistema.handlers

object FuncionarioHandler: CrudHandler {
    override fun cadastrar() {
        println("Funcionario cadastrado")
    }

    override fun consultar() {
        println("Funcionario consultado")
    }
    override fun alterar() {
        println("Funcionario alterado")
    }
    override fun excluir() {
        println("Funcionario excluido")
    }

}