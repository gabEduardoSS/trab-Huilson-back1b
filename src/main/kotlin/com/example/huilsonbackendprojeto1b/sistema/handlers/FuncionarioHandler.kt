package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import com.example.huilsonbackendprojeto1b.service.FuncionarioService
import com.example.huilsonbackendprojeto1b.utils.validarCampoData
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import com.example.huilsonbackendprojeto1b.utils.validarCampoString
import java.time.LocalDate

class FuncionarioHandler(
    private val funcionarioService: FuncionarioService
): OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Cadastrar" to { cadastrarFuncionario() },
        "Consultar ativos" to { consultarFuncionariosStatus("ativo")},
        "Consultar desativados" to { consultarFuncionariosStatus("desativado")},
        "Consultar todos" to { consultarFuncionarios() },
        "Alterar" to { alterarFuncionario() },
        "Desativar" to { alterarStatus("desativar") },
        "Reativar" to { alterarStatus("ativar") }
    )

    private fun cadastrarFuncionario() {
        println("----<| Cadastrar Funcionário |>----")
        val nome = validarCampoString("Digite o nome do funcionário: ")

        var cpf: String
        do {
            cpf = validarCampoNumerico("Digite o CPF do funcionário: ", tipo = 3)
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

        var dtNasc: LocalDate
        do{
            dtNasc = validarCampoData("Digita a data de nascimento: ")!!
            if(dtNasc > LocalDate.now()){
                println("Data de nascimento inválida")
                continue
            }
            break
        } while(true)

        val salario = validarCampoNumerico("Digite o salário: ").toBigDecimal()

        var turno: Turno
        Turno.entries.forEach { t ->
            println("${t.ordinal} - ${t.name.replace("_", " ")}")
        }
        do {
            val codigoTurno = validarCampoNumerico("Escolha o turno: ", tipo = 1).toInt()
            if (codigoTurno !in Turno.entries.indices) {
                println("Código do turno não existe")
                continue
            }
            turno = Turno.entries[codigoTurno]
            break
        } while (true)

        var cargo: Cargo
        Cargo.entries.forEach { c ->
            println("${c.ordinal} - ${c.name.replace("_", " ")}")
        }
        do {
            val codigo = validarCampoNumerico("Escolha o cargo: ", tipo = 1).toInt()
            if (codigo !in Cargo.entries.indices) {
                println("Código do turno não existe")
                continue
            }
            cargo = Cargo.entries[codigo]
            break
        } while (true)

        val funcionario = Funcionario(
            nome = nome,
            cpf = cpf,
            email = email,
            telefone = telefone,
            cidade = cidade,
            endereco = endereco,
            dtNasc = dtNasc,
            salario = salario,
            turno = turno,
            cargo = cargo
        )

        try{
            funcionarioService.salvarFuncionario(funcionario)
        } catch(e: Exception){
            println("Erro ao cadastrar: $e")
        }
    }

    private fun consultarFuncionarios(){
        val funcionarios = funcionarioService.listarFuncionarios()
        if (funcionarios.isEmpty()) {
            println("Não há funcionários cadastrados")
            return
        }

        println("----<| Funcionários cadastrados |>----")
        funcionarios.forEach { funcionario ->
            println(funcionario.valores())
        }
    }

    private fun consultarFuncionariosStatus(status: String) {
        val funcionarios = funcionarioService.consultarPorStatus(status)
        if (funcionarios.isEmpty()) {
            if (status == "ativo") {
                print("Não há funcionários ativos")
            } else if (status == "desativado") {
                print("Não há funcionários desativados")
            }
            return
        }

        println("----<| Funcionários $status |>----")
        funcionarios.forEach { funcionario ->
            println(funcionario.valores())
        }
    }

    private fun alterarFuncionario() {
        val funcionarios = funcionarioService.listarFuncionarios()
        val IDs = mutableListOf<Long?>()

        if (funcionarios.isEmpty()) {
            println("Não há funcionários cadastrados")
            return
        }

        println("----<| Funcionários Cadastrados |>----")
        funcionarios.forEach { funcionario ->
            IDs.add(funcionario.id)
            println(funcionario.valores())
        }

        var idFuncionario: Long
        do {
            idFuncionario = validarCampoNumerico("Insira o ID do funcionário a ser alterado: ", tipo = 1).toLong()
            if (idFuncionario !in IDs) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        val funcionarioAtual = funcionarios.first { it.id == idFuncionario }

        println("Deixe em branco para manter o valor atual: ")

        val nomeInput = validarCampoString("Digite o nome(atual: ${funcionarioAtual.nome}): ", aceitarBranco = true)
        val nome = nomeInput.ifBlank { funcionarioAtual.nome }

        val cpfInput = validarCampoNumerico("Digite o CPF(atual: ${funcionarioAtual.cpf}): ", aceitarBranco = true)
        val cpf = cpfInput.ifBlank { funcionarioAtual.cpf }

        val emailInput = validarCampoString("Digite o email(${funcionarioAtual.email}): ", aceitarBranco = true)
        val email = emailInput.ifBlank { funcionarioAtual.email }

        val telefoneInput = validarCampoNumerico("Digite o telefone(${funcionarioAtual.telefone}): ", aceitarBranco = true, tipo = 3)
        val telefone = telefoneInput.ifBlank { funcionarioAtual.telefone }

        val cidadeInput = validarCampoString("Digite a cidade(${funcionarioAtual.cidade}): ", aceitarBranco = true, tipo = 3)
        val cidade = cidadeInput.ifBlank { funcionarioAtual.cidade }

        val enderecoInput = validarCampoString("Digite o endereco(${funcionarioAtual.endereco}): ", aceitarBranco = true, tipo = 4)
        val endereco = enderecoInput.ifBlank { funcionarioAtual.endereco }

        val dtNascInput = validarCampoData("Digite a dt nascimento(atual: ${funcionarioAtual.dtNasc}): ", aceitarBranco = true)
        val dtNasc = dtNascInput ?: funcionarioAtual.dtNasc

        val salarioInput = validarCampoNumerico("Digite o salário(atual: ${funcionarioAtual.salario}): ", aceitarBranco = true)
        val salario = if (salarioInput.isBlank()) funcionarioAtual.salario else salarioInput.toBigDecimal()

        println("Escolha o turno(atual: ${funcionarioAtual.turno}) — deixe em branco para manter: ")
        var turno = funcionarioAtual.turno
        Turno.entries.forEach { t ->
            println("${t.ordinal} - ${t.name.replace("_", " ")}")
        }
        do {
            val turnoInput = validarCampoNumerico("Escolha o turno: ", tipo = 1, aceitarBranco = true)
            if (turnoInput.isBlank()) {
                break
            }
            val codigoTurno = turnoInput.toInt()
            if (codigoTurno !in Turno.entries.indices) {
                println("Código do turno não existe")
                continue
            }
            turno = Turno.entries[codigoTurno]
            break
        } while (true)

        println("Escolha o cargo(atual: ${funcionarioAtual.cargo}) — deixe em branco para manter: ")
        var cargo = funcionarioAtual.cargo
        Cargo.entries.forEach { c ->
            println("${c.ordinal} - ${c.name.replace("_", " ")}")
        }
        do {
            val cargoInput = validarCampoNumerico("Escolha o cargo: ", tipo = 1, aceitarBranco = true)
            if (cargoInput.isBlank()) {
                break
            }
            val codigoCargo = cargoInput.toInt()
            if (codigoCargo !in Cargo.entries.indices) {
                println("Código do cargo não existe")
                continue
            }
            cargo = Cargo.entries[codigoCargo]
            break
        } while (true)

        val funcionarioAtualizado = Funcionario(
            nome = nome,
            cpf = cpf,
            email = email,
            telefone = telefone,
            cidade = cidade,
            endereco = endereco,
            dtNasc = dtNasc!!,
            salario = salario,
            turno = turno,
            cargo = cargo,
        )

        try {
            funcionarioService.alterarFuncionario(idFuncionario, funcionarioAtualizado)
            println("Funcionário atualizado: ${funcionarioAtualizado.valores()}")
        } catch (e: Exception) {
            println("Erro ao atualizar o funcionário: $e")
        }
    }

    fun alterarStatus(tipo: String){
        val status = if (tipo == "ativar") "ativo" else "desativado"
        val stringConsulta = if (tipo == "ativar") "desativado" else "ativo"
        val funcionarios = funcionarioService.consultarPorStatus(stringConsulta)
        val IDs: MutableList<Long?> = mutableListOf()

        if (funcionarios.isEmpty()) {
            print("Não há funcionários ${stringConsulta}s")
            return
        }

        println("----<| Funcionários ${stringConsulta}s |>----")
        funcionarios.forEach { funcionario ->
            IDs.add(funcionario.id)
            println(funcionario.valores())
        }

        var idFuncionario: Long = 0
        do {
            print("Insira o ID do funcionario a ser $status: ")
            idFuncionario = readln().toLong()
            if (idFuncionario !in IDs) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        funcionarioService.alterarStatus(idFuncionario, status)
    }
}