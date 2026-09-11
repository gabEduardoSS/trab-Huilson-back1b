package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal

class ItemCompra (
    var id: Long? = null,
    var movimentacao: Movimentacao? = null,
    val produto: CaixaDeAgua,
    val precoUnitario: BigDecimal = produto.preco,
    val quantidade: Int,
){
    fun setValues(id: Long, movimentacao: Movimentacao) {
        this.id = id
        this.movimentacao = movimentacao

    }
    fun valores(){
        println("""
        ---------<| ITEM |>---------
            ID item: $id,
            ID compra: $id,
            ID movimentacao: ${movimentacao?.id},
            ID produto: ${produto.id},
            Preço: ${formatacaoDinheiro(precoUnitario)},
            Valor Total: ${formatacaoDinheiro(precoUnitario * (quantidade).toBigDecimal())},
            Quantidade: $quantidade
        ----------------------------
        """.trimIndent())
    }
}