package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.Habilidade
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.pessoas.Instalador
import java.math.BigDecimal
import java.time.LocalDate

class Servico {
    var instalador : Instalador = Instalador(
        nome = "",
        cpf = "",
        idade = 0,
        salario = BigDecimal.ZERO,
        turno = Turno.NOTURNO,
        habilidade = Habilidade.INSTALACAO
    )
    var preco : String = "0.0"
    var dataInstalacao : LocalDate = LocalDate.of(1970, 7, 4)
    var cliente : Cliente = Cliente(
        nome = "",
        cpf = "",
        idade = 0,
        dividasAbertas = false,
        parcelasAPagar = mutableListOf()
    )
}