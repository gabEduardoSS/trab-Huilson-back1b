package com.example.huilsonbackendprojeto1b.pessoas

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import java.time.LocalDate
import java.time.LocalDateTime

open class Pessoa (
    open var id: Long? = null,

    open var nome: String = "",
    open var cpf: String = "",
    open var email: String = "",
    open var telefone: String = "",
    open var cidade: String = "",
    open var endereco: String = "",
    open var dtNasc: LocalDate? = LocalDate.of(9999, 1, 1),

    open var tipo: TipoPessoa = TipoPessoa.CLIENTE,

    open var dtCriacao: LocalDateTime = LocalDateTime.now(),
){}