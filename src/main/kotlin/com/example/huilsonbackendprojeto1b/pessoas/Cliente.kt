package com.example.huilsonbackendprojeto1b.pessoas

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Cliente(
    nome: String = "",
    cpf: String = "",
    dtNasc: LocalDate? = null,
    var dividasAbertas: Boolean = false,
    val dtCriacao: LocalDate = LocalDate.now(),
    var status: String = "ativo"
) : Pessoa(
    nome = nome,
    cpf = cpf,
    dtNasc = dtNasc,
    tipo = TipoPessoa.CLIENTE,
){
}