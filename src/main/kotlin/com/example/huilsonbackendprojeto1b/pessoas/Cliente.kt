package com.example.huilsonbackendprojeto1b.pessoas

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

//@Entity
class Cliente(
    nome: String = "",
    cpf: String = "",
    idade: Int = 0,
    var dividasAbertas: Boolean? = null,
    var parcelasAPagar : MutableList<BigDecimal>? = null,
) : Pessoa(
    nome = nome,
    cpf = cpf,
    idade = idade,
    tipo = TipoPessoa.CLIENTE
){
}