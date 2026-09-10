package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.StatusFinanceiro
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Venda (
    var id: Long? = null,

    var valorTotal: BigDecimal = BigDecimal.ZERO,
    val vendedor: Funcionario,
    val cliente: Cliente,
    val valorDesconto: BigDecimal = BigDecimal.ZERO,
    var status: String? = null,
    val descricao: String? = null,
    var data: LocalDateTime? = null,

    var itensVenda: MutableList<ItemVenda> = mutableListOf(),
){
    fun adicionarItem(produto: CaixaDeAgua, quantidade: Int) {
        itensVenda.add(ItemVenda(
            produto = produto,
            quantidade = quantidade,
        ))
        valorTotal += produto.preco * quantidade.toBigDecimal()
    }

    fun valores(){
        print("""
            ------<| VENDA |>------
            ID: $id,
            Valor Total: ${formatacaoDinheiro(valorTotal)},
            Status Venda: $status,
            Descricao: $descricao,
            Vendedor: ${vendedor.id},
            Desconto: ${formatacaoDinheiro(valorDesconto)},
            Data: ${data?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}
            -----------------------
            
        """.trimIndent())
        itensVenda.forEach { item ->
            item.valores()
        }
        println()
    }
}