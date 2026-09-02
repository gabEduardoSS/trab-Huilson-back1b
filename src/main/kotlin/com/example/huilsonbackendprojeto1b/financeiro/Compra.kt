package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.StatusFinanceiro
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.math.BigDecimal

class Compra(
    val id: Long? = null,

    val valorTotal: BigDecimal,
    val requisitor: Funcionario,
    val valorDesconto: BigDecimal = BigDecimal.ZERO,
    val statusCompra: StatusFinanceiro = StatusFinanceiro.CONCLUIDA,

    var itensCompra: MutableList<ItemCompra>
) {
    fun adicionarItem(produto: CaixaDeAgua) {
        itensCompra.add(ItemCompra(
            produto = produto,
        ))
    }
}