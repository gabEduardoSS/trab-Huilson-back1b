package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.service.ClienteService
import com.example.huilsonbackendprojeto1b.utils.validarCampoString
import com.example.huilsonbackendprojeto1b.utils.validarCampoData
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import java.time.LocalDate

class ClienteHandler(
    private val clienteService: ClienteService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Cadastrar" to {cadastrarCliente() },
        "Consultar" to {consultarClientes()},
        "Alterar" to {alterarCliente()},
    )

    private fun cadastrarCliente(){
        println("----<| Cadastrar Cliente |>----")
        val nome = validarCampoString("Digite o nome do cliente: ")

        var cpf: String
        do {
            cpf = validarCampoNumerico("Digite o CPF do cliente: ", tipo = 3)
            if(cpf.length != 11){
                println("CPF inválido")
                continue
            }
            break
        } while (true)

        val email: String = validarCampoString("Digite o email: ", tipo = 2)

        val telefone: String = validarCampoNumerico("Digite o telefone(apenas números): ", tipo = 3)

        val cidade: String = validarCampoString("Digite a cidade e o estado(use cidade, estado): ", tipo = 3)

        val endereco: String = validarCampoString("Digite o endereco(use rua, número): ", tipo = 4)

        var dtNasc: LocalDate?
        do{
            dtNasc = validarCampoData("Digita a data de nascimento(ou deixe em branco): ", aceitarBranco = true)
            if (dtNasc == null) break
            if(dtNasc > LocalDate.now()){
                println("Data de nascimento inválida")
                continue
            }
            break
        } while(true)

        val cliente = Cliente(
            nome = nome,
            cpf = cpf,
            email = email,
            telefone = telefone,
            cidade = cidade,
            endereco = endereco,
            dtNasc = dtNasc,
        )

        try{
            clienteService.salvarCliente(cliente)
        } catch(e: Exception){
            println("Erro ao cadastrar: $e")
        }
    }

    private fun consultarClientes(){
        println("----<| Clientes |>----")
        val clientes = clienteService.listarClientes()
        if(clientes.isEmpty()){
            println("Não há clientes cadastrados")
            return
        }
        clientes.forEach {cliente ->
            println(cliente.valores())
        }
    }

    private fun alterarCliente(){
        val clientes = clienteService.listarClientes()

        if(clientes.isEmpty()){
            println("Não há clientes cadastrados")
            return
        }

        println("----<| Clientes Cadastrados |>----")
        clientes.forEach { cliente ->
            println(cliente.valores())
        }

        var idCliente: Long
        do {
            idCliente = validarCampoNumerico("Insira o ID do cliente a ser alterado: ", tipo = 1).toLong()
            if (clientes.none{ it.id == idCliente }) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        val clienteAtual = clientes.first { it.id == idCliente }

        println("Deixe em branco para manter o valor atual: ")

        val nomeInput = validarCampoString("Digite o nome(atual: ${clienteAtual.nome}): ", aceitarBranco = true)
        val nome = nomeInput.ifBlank { clienteAtual.nome }

        val cpfInput = validarCampoNumerico("Digite o CPF(atual: ${clienteAtual.cpf}): ", aceitarBranco = true)
        val cpf = cpfInput.ifBlank{ clienteAtual.cpf }

        val emailInput = validarCampoString("Digite o email(${clienteAtual.email}): ", aceitarBranco = true)
        val email = emailInput.ifBlank { clienteAtual.email }

        val telefoneInput = validarCampoNumerico("Digite o telefone(${clienteAtual.telefone}): ", aceitarBranco = true, tipo = 3)
        val telefone = telefoneInput.ifBlank { clienteAtual.telefone }

        val cidadeInput = validarCampoString("Digite a cidade(${clienteAtual.cidade}): ", aceitarBranco = true, tipo = 3)
        val cidade = cidadeInput.ifBlank { clienteAtual.cidade }

        val enderecoInput = validarCampoString("Digite o endereco(${clienteAtual.endereco}): ", aceitarBranco = true, tipo = 4)
        val endereco = enderecoInput.ifBlank { clienteAtual.endereco }

        val dtNascInput = validarCampoData("Digite a dt nascimento(atual: ${clienteAtual.dtNasc}): ", aceitarBranco = true)
        val dtNasc = dtNascInput ?: clienteAtual.dtNasc

        val clienteAtualizado = Cliente(
            nome = nome,
            cpf = cpf,
            email = email,
            telefone = telefone,
            cidade = cidade,
            endereco = endereco,
            dtNasc = dtNasc,
        )

        try{
            clienteService.alterarCliente(idCliente, clienteAtualizado)
            println("Cliente atualizado: ${clienteAtualizado.valores()}")
        } catch(e: Exception){
            println("Erro ao atualizar o cliente : $e")
        }

    }
}