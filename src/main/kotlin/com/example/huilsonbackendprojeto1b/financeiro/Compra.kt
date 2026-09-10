package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Compra(
    var id: Long? = null,

    var valorTotal: BigDecimal = BigDecimal.ZERO,
    val requisitor: Funcionario,
    val valorDesconto: BigDecimal = BigDecimal.ZERO,
    var status: String? = null,
    val descricao: String? = null,
    var data: LocalDateTime? = null,

    var itensCompra: MutableList<ItemCompra> = mutableListOf(),
) {
    fun adicionarItem(produto: CaixaDeAgua, quantidade: Int) {
        itensCompra.add(ItemCompra(
            produto = produto,
            quantidade = quantidade,
        ))
        valorTotal += produto.preco * quantidade.toBigDecimal()
    }

    fun valores(){
        print("""
            ------<| COMPRA |>------
            ID: $id,
            Valor Total: ${formatacaoDinheiro(valorTotal)},
            Status Compra: $status,
            Descricao: $descricao,
            Funcionario: ${requisitor.id},
            Desconto: ${formatacaoDinheiro(valorDesconto)},
            Data: ${data?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}
            ------------------------
            
        """.trimIndent())
        itensCompra.forEach { item ->
            item.valores()
        }
        println()
    }
}
