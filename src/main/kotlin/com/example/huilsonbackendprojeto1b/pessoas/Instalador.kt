package com.example.huilsonbackendprojeto1b.pessoas


import com.example.huilsonbackendprojeto1b.enumeradores.Habilidade
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import java.math.BigDecimal

class Instalador (
    nome : String,
    cpf : String,
    idade : Int,
    val salario : BigDecimal,
    val turno : Turno,
    val habilidade : Habilidade,
) : Pessoa(
    nome = nome,
    cpf = cpf,
    idade = idade,
    tipo = TipoPessoa.FUNCIONARIO,
) {

}