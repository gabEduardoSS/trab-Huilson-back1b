package com.example.huilsonbackendprojeto1b.sistema.handlers

object ClienteHandler: CrudHandler {
    override fun cadastrar() {
        println("Cliente cadastrado")
    }

    override fun consultar() {
        println("Cliente consultado")
    }
    override fun alterar() {
        println("Cliente alterado")
    }
    override fun excluir() {
        println("Cliente excluido")
    }

}