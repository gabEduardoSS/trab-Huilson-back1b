package com.example.huilsonbackendprojeto1b.pessoas

import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType

//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
open class Pessoa (
    //@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    open var nome: String = "",
    open var cpf: String = "",
    open var idade: Int = 0,

    //@Enumerated(EnumType.STRING)
    open var tipo: TipoPessoa = TipoPessoa.CLIENTE
){}