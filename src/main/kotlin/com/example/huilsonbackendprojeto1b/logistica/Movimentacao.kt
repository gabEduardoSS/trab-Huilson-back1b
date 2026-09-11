package com.example.huilsonbackendprojeto1b.logistica

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class Movimentacao(
    var id: Long? = null,

    val produto: CaixaDeAgua,
    val quantidade: Int,
    val descricao: String? = null,
    val tipo: TipoMovimentacao
){
    var quantidadeAnterior: Int? = null
    var quantidadePosterior: Int? = null
    var status: String? = null
    var dataMovimentacao: LocalDateTime? = null

    fun valores(){
        println("""
            --------<| MOVIMENTAÇÃO |>--------
            Quantidade: ${quantidade},
            ID do Produto: ${produto.id},
            Tipo: ${tipo.name},
            Descricao: ${descricao},
            Quantidade Anterior: ${quantidadeAnterior},
            Quantidade Posterior: ${quantidadePosterior},
            Status: ${status},
            Data: ${dataMovimentacao?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))}
            ----------------------------------
        """.trimIndent())
    }
}
