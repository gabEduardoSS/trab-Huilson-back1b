package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.utils.formatacaoDinheiro
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class CaixaDeAgua(
    var id: Long? = 0,

    var marca : String = "",
    var modelo : String = "",
    var dimensao : MutableList<Double> = mutableListOf(0.0, 0.0, 0.0),

    var cor : Cor = Cor.AZUL_FRACO,

    var material : Material = Material.FIBRA_DE_VIDRO,

    var formato : Formatos = Formatos.CONICO,

    var fornecedor : String = "",
    var preco : BigDecimal = BigDecimal.ZERO,

    var status: String = "ativo",

    var quantidadeMinima: Int? = 0,
    var quantidadeMaxima: Int? = 0,
    var quantidade: Int = 0,

    val dtCriacao: LocalDateTime = LocalDateTime.now()
){
    open fun valores() : String{
        return """
            
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
        Estoque Minimo: $quantidadeMinima,
        Estoque Máximo: $quantidadeMaxima,
        Data de Criação: ${dtCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))},
        ---------------------
        
        """.trimIndent()
    }
}