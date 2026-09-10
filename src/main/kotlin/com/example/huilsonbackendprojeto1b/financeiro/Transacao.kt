package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.time.LocalDateTime

class Transacao(
    var id: Long? = null,

    val valor : BigDecimal,
    val pessoa : Pessoa,
    val tipoTransacao : TipoTransacao,
){
    var saldoAnterior: BigDecimal? = null
    var saldoPosterior: BigDecimal? = null
    var status: String? = null

    var dataMovimentacao : LocalDateTime? = null

    fun valores(){
        print("""
            Valor: ${formatacaoDinheiro(valor)},
            ID da Pessoa: ${pessoa.id},
            Tipo: ${tipoTransacao.name},
            Saldo Anterior: ${saldoAnterior},
            Saldo Posterior: ${saldoPosterior},
            Status: ${status},
            Data: $dataMovimentacao
        """.trimIndent())
    }
}
