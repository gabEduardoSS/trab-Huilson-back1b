package com.example.huilsonbackendprojeto1b.pessoas

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class Pessoa (
    open var id: Long? = null,

    open var nome: String = "",
    open var cpf: String = "",
    open var email: String = "",
    open var telefone: String = "",
    open var cidade: String = "",
    open var endereco: String = "",
    open var dtNasc: LocalDate?,

    open var tipo: TipoPessoa = TipoPessoa.CLIENTE,

    open var dtCriacao: LocalDateTime = LocalDateTime.now(),
){
    open fun valores(){
        println("""
            -----------<| PESSOA |>-----------
            ID: $id,
            Nome: $nome,
            CPF: $cpf,
            Email: $email,
            Telefone: $telefone,
            Endereco: $cidade - $endereco,
            Tipo: ${tipo.name},
            Data de Nascimento: ${if (dtNasc != null) dtNasc!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) else "null"},
            ----------------------------------
        """.trimIndent())
    }
}