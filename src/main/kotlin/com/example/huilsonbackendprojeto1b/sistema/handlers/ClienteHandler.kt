package com.example.huilsonbackendprojeto1b.sistema.handlers

import org.springframework.stereotype.Component

@Component
object ClienteHandler: OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Cadastrar" to ::cadastrar,
        "Consultar" to ::consultar,
        "Alterar" to ::alterar,
        "Excluir" to ::excluir
    )

    private fun cadastrar(){
        println("Cliente cadastrado")
    }

    private fun consultar(){
        println("Consultar Clientes")
    }

    private fun alterar(){
        println("Cliente alterado")
    }

    private fun excluir(){
        println("Cliente excluido")
    }
}