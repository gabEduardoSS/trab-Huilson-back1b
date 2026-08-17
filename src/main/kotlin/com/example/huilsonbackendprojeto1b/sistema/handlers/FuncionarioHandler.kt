package com.example.huilsonbackendprojeto1b.sistema.handlers

object FuncionarioHandler: OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Cadastrar" to ::cadastrar,
        "Consultar" to ::consultar,
        "Alterar" to ::alterar,
        "Excluir" to ::excluir
    )

    private fun cadastrar(){
        println("Funcionário cadastrado")
    }

    private fun consultar(){
        println("Consultar funcionários")
    }

    private fun alterar(){
        println("Funcionario alterado")
    }

    private fun excluir(){
        println("Funcionario excluido")
    }
}