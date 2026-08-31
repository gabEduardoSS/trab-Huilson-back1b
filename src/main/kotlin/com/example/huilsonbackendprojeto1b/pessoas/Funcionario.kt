package com.example.huilsonbackendprojeto1b.pessoas


import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Funcionario (
    nome : String = "",
    cpf : String = "",
    email : String = "",
    telefone : String = "",
    cidade : String = "",
    endereco : String = "",
    dtNasc : LocalDate = LocalDate.of(1969, 1, 1),
    var salario : BigDecimal = "1712.00".toBigDecimal(),

    var turno : Turno = Turno.MATUTINO,

    var cargo: Cargo = Cargo.ATENDIMENTO,

    var status: String = "ativo"
) : Pessoa(
    nome = nome,
    cpf = cpf,
    email = email,
    telefone = telefone,
    cidade = cidade,
    endereco = endereco,
    dtNasc = dtNasc,
    tipo = TipoPessoa.FUNCIONARIO,
) {
    fun valores(): String{
        return """
            
            ID: $id,
            Nome: $nome,
            CPF: $cpf,
            Email: $email,
            Telefone: $telefone,
            Endereco: $cidade - $endereco,
            Data de Nascimento: ${if(dtNasc != null) dtNasc!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) else "null"},
            Salario: ${formatacaoDinheiro(salario)},
            Turno: $turno,
            Cargo: $cargo,
            Status: $status,
            Data do Cadastro: ${dtCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))},
            ---------------------
            
        """.trimIndent()
    }
}