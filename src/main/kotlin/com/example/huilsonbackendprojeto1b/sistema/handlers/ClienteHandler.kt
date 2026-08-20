package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.utils.userInput
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import org.springframework.stereotype.Component

@Component
object ClienteHandler: OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Cadastrar" to {cadastrarCliente() },
        "Consultar" to {consultar()},
        "Alterar" to {alterar()},
    )

    private fun cadastrarCliente(){
        var nome = userInput("Digite o nome do cliente: ")

        var cpf: String
        do {
            cpf = validarCampoNumerico("Digite o CPF do cliente(apenas números): ", aceitarDecimal = false)
            if(cpf.length != 11){
                println("CPF inválido")
                continue
            }
            break
        } while (true)


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