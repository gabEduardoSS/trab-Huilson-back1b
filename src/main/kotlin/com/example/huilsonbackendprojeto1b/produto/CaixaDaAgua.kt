package com.example.huilsonbackendprojeto1b.produto

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Habilidade
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.enumeradores.Turno
import com.example.huilsonbackendprojeto1b.pessoas.Instalador
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal


@Entity
open class CaixaDaAgua(
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
    var preco : String = BigDecimal.ZERO.toString()
){
    open fun valores() : String{
        return """
            $marca
            $modelo
            $dimensao
            $cor
            $material
            $formato
            $fornecedor
            $preco
        """.trimIndent()
    }
}