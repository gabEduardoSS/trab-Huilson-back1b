package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import java.math.BigDecimal
import java.time.LocalDateTime
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro as format

class Transacao(
    val caixa: Caixa,
    val valor : BigDecimal,
    val pessoa : Pessoa,
    val dataMovimentacao : LocalDateTime,
){
    val tipoMovimentacao : TipoMovimentacao = when (pessoa.tipo) {
        TipoPessoa.FUNCIONARIO -> TipoMovimentacao.SAIDA;
        TipoPessoa.CLIENTE -> TipoMovimentacao.ENTRADA
    }

    fun transacao() : String{
        val valorAnterior = format(caixa.saldo)
        val valorTransacao = valor * pessoa.tipo.multiplicador
        caixa.saldo += valorTransacao
        return "\nTransação bem sucedida:\n" +
                "   Data/Hora: $dataMovimentacao\n" +
                "   Tipo: ${tipoMovimentacao.getString()}\n" +
                "   Saldo: R$${valorAnterior} -> R$${format(caixa.saldo)} (R$${format(valorTransacao)})\n" +
                "   Nome: ${pessoa.nome}, CPF: ${pessoa.cpf}\n"
    }
}