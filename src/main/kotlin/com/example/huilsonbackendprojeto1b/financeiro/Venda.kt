package com.example.huilsonbackendprojeto1b.financeiro

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.pessoas.Funcionario
import java.time.LocalDateTime

class Venda (
    var id: Long? = null,

    var valorTotal: Double,
    var cliente: Cliente,
    var vendedor: Funcionario,
    var valorDesconto: Double = 0.0,
    var statusVenda: String = "ativo",

    var itensVenda: MutableList<ItemVenda>,

    var dtCriacao: LocalDateTime = LocalDateTime.now(),
){
    fun adicionarItem(){}
}