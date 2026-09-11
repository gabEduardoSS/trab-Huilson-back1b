package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formato
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class CaixaDeAgua(
    var id: Long? = 0,

    var marca : String = "",
    var modelo : String = "",
    var dimensao : MutableList<Double> = mutableListOf(0.0, 0.0, 0.0),

    var cor : Cor = Cor.AZUL_FRACO,

    var material : Material = Material.FIBRA_DE_VIDRO,

    var formato : Formato = Formato.CONICO,

    var fornecedor : String = "",
    var preco : BigDecimal = BigDecimal.ZERO,

    var status: String = "ativo",

    var quantidade: Int = 0,

    val dtCriacao: LocalDateTime = LocalDateTime.now()
){
    open fun valores() {
        println("""
            ----------<| PRODUTO |>----------
            ID: $id,
            Marca: $marca,
            Modelo: $modelo,
            Dimensão(AxLxP): $dimensao,
            Cor: $cor,
            Material: $material,
            Formato: $formato,
            Fornecedor: $fornecedor,
            Preço: ${formatacaoDinheiro(preco)}
            Status: $status
            Estoque Atual: $quantidade,
            Data de Criação: ${dtCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))},
            ---------------------------------
        """.trimIndent())
    }
}