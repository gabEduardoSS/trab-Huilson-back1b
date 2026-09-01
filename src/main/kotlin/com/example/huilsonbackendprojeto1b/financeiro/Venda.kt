package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.StatusFinanceiro
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import java.math.BigDecimal
import java.time.LocalDateTime

class Venda (
    var id: Long? = null,

    var valorTotal: Double,
    var cliente: Cliente,
    var vendedor: Funcionario,
    var valorDesconto: BigDecimal = BigDecimal.ZERO,
    var statusVenda: StatusFinanceiro = StatusFinanceiro.CONCLUIDA,

    var itensVenda: MutableList<ItemVenda>,

    var dtCriacao: LocalDateTime = LocalDateTime.now(),
){
    fun adicionarItem(){}
}