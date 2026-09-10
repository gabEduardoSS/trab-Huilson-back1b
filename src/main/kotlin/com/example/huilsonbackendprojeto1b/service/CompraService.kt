package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.financeiro.Compra
import com.example.huilsonbackendprojeto1b.repositorio.JPACompra

class CompraService {
    fun realizarCompra(compra: Compra): Compra? {
        return JPACompra.criarCompra(compra)
    }
}
