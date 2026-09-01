package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.math.BigDecimal

class ItemCompra (
    val id: Long? = null,
    val id_compra: Long? = null,
    val produto: CaixaDeAgua,
    val precoUnitario: BigDecimal = produto.preco,
)