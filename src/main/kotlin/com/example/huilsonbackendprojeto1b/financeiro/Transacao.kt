package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import java.math.BigDecimal
import java.time.LocalDateTime
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro as format

class Transacao(
    val id: Long? = null,

    val caixa: Caixa,
    val valor : BigDecimal,
    val pessoa : Pessoa,
    val tipoTransacao : TipoTransacao,

    val dataMovimentacao : LocalDateTime,
){
    fun verificarSaldo(): Boolean{
        if(tipoTransacao == TipoTransacao.SAIDA && valor > caixa.saldo){
            println("Saldo insuficiente, faltam ${format(valor-caixa.saldo)} para ser possível a compra")
        }
        return true
    }

    fun transacao() : String{
        val valorAnterior = format(caixa.saldo)
        val valorTransacao = valor * tipoTransacao.valor
        caixa.saldo += valorTransacao
        return "\nTransação bem sucedida:\n" +
                "   Data/Hora: $dataMovimentacao\n" +
                "   Tipo: ${tipoTransacao}\n" +
                "   Saldo: R$${valorAnterior} -> R$${format(caixa.saldo)} (R$${format(valorTransacao)})\n" +
                "   Nome: ${pessoa.nome}, CPF: ${pessoa.cpf}\n"
    }
}