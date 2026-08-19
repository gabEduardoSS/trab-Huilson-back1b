package com.example.huilsonbackendprojeto1b.pessoas


import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal
import java.time.LocalDate

//@Entity
class Funcionario (
    nome : String = "",
    cpf : String = "",
    dtNasc : LocalDate = LocalDate.of(1969, 1, 1),
    val salario : BigDecimal = "1712.00".toBigDecimal(),

    @Enumerated(EnumType.STRING)
    val turno : Turno = Turno.MATUTINO,

    @Enumerated(EnumType.STRING)
    val cargo: Cargo = Cargo.ATENDENTE,
) : Pessoa(
    nome = nome,
    cpf = cpf,
    dtNasc = dtNasc,
    tipo = TipoPessoa.FUNCIONARIO,
) {

}