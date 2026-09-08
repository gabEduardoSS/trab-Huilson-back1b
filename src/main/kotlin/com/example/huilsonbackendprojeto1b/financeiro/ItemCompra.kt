package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal

class ItemCompra (
    val id: Long? = null,
    val id_compra: Long? = null,
    val produto: CaixaDeAgua,
    val precoUnitario: BigDecimal = produto.preco,
    val quantidade: Int,
){
    fun valores(){
        println("""
        ---------<| ITEM |>---------
            ID item: $id,
            ID compra: $id_compra,
            ID produto: ${produto.id},
            Preço: ${formatacaoDinheiro(produto.preco)},
            Quantidade: $quantidade
        ----------------------------
        """.trimIndent())
    }
}