package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal


@Entity
@Table(name = "caixa_de_agua")
open class CaixaDeAgua(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    var marca : String = "",
    var modelo : String = "",
    var dimensao : MutableList<Double> = mutableListOf(0.0, 0.0, 0.0),

    @Enumerated(EnumType.STRING)
    var cor : Cor = Cor.AZUL_FRACO,

    @Enumerated(EnumType.STRING)
    var material : Material = Material.FIBRA_DE_VIDRO,

    @Enumerated(EnumType.STRING)
    var formato : Formatos = Formatos.CONICO,

    /*var instalador : Instalador = Instalador(
        nome = "",
        cpf = "",
        idade = 0,
        salario = BigDecimal.ZERO,
        turno = Turno.NOTURNO,
        habilidade = Habilidade.INSTALACAO
    )*/
    var fornecedor : String = "",
    var preco : String = BigDecimal.ZERO.toString(),
    var status: String = "ativado"
){
    open fun valores() : String{
        return """
            
        ID: $id,
        marca: $marca,
        modelo: $modelo,
        dimensão(AxLxP): $dimensao,
        cor: $cor,
        material: $material,
        formato: $formato,
        fornecedor: $fornecedor,
        preço: $preco
        status: $status
        ---------------------
        
        """.trimIndent()
    }
}