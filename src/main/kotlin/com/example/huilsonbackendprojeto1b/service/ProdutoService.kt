package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.repositorio.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class ProdutoService(
    val repository: ProdutoRepository
) {
    fun cadastrarProduto(produto: CaixaDeAgua): CaixaDeAgua {
        return repository.save(produto)
    }

    fun consultarProdutos() : List<CaixaDeAgua> {
        return repository.findAll()
    }
}