package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import java.math.BigDecimal
import java.time.LocalDateTime
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro as format

class Transacao(
    val caixa: Caixa,
    val valor : BigDecimal,
    val pessoa : Pessoa,
    val dataMovimentacao : LocalDateTime = LocalDateTime.now(),
    val tipoTransacao : TipoTransacao
){
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