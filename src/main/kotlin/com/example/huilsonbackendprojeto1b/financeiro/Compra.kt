package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.StatusFinanceiro
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.repositorio.JPACompra
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.repositorio.JPAMovimentacao
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException
import java.time.LocalDateTime

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

    fun compra(){
        var con: Connection? = null
        try{
            con = JPAConexao.conectar()

            val retorno = JPACompra.criarCompra(this, con)

            id = (retorno?.getValue("id") as Int?)?.toLong()
            status = retorno?.getValue("status") as String?
            data = retorno?.getValue("data") as LocalDateTime?
        } catch(e: SQLException){
            println("ERRO: ${e.stackTrace.joinToString(", ")}")
        } finally {
            con?.close()
        }
    }

    fun valores(){
        print("""
            -----------------------
            ID: $id,
            Valor Total: ${formatacaoDinheiro(valorTotal)},
            Status Compra: $status,
            Descricao: $descricao,
            Funcionario: ${requisitor.id},
            Desconto: ${formatacaoDinheiro(valorDesconto)},
            ------------------------
            
        """.trimIndent())
        itensCompra.forEach { item ->
            item.valores()
        }
    }
}