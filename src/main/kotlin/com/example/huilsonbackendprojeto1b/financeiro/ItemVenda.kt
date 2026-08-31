package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.math.BigDecimal

class ItemVenda (
    var id: Long? = null,
    var produto: CaixaDeAgua,
    var precoUnitario: BigDecimal,
)