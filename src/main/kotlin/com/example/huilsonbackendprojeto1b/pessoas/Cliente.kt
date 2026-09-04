package com.example.huilsonbackendprojeto1b.pessoas

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Cliente(
    nome: String,
    cpf: String,
    email: String,
    telefone: String,
    cidade: String,
    endereco: String,
    dtNasc: LocalDate? = null,
    var dividasAbertas: Boolean = false,
) : Pessoa(
    nome = nome,
    cpf = cpf,
    email = email,
    telefone = telefone,
    cidade = cidade,
    endereco = endereco,
    dtNasc = dtNasc,
    tipo = TipoPessoa.CLIENTE,
){
    fun valores(): String{
        return """
            
            ID: $id,
            Nome: $nome,
            CPF: $cpf,
            Email: $email,
            Telefone: $telefone,
            Endereco: $cidade - $endereco,
            Data de Nascimento: ${if(dtNasc != null) dtNasc!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) else "null"},
            Dividas em aberto: ${if(dividasAbertas) "SIM" else "NÃO"}
            Data do Cadastro: ${dtCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))},
            ---------------------
            
        """.trimIndent()
    }
}