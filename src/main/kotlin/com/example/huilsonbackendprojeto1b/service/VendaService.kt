package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.financeiro.Venda
import com.example.huilsonbackendprojeto1b.repositorio.JPAVenda

class VendaService {
    fun realizarVenda(venda: Venda): Venda? {
        return JPAVenda.criarVenda(venda)
    }
}
