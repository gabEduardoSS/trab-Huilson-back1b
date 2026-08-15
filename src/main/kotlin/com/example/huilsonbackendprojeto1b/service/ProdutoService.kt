package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.produto.CaixaDaAgua
import com.example.huilsonbackendprojeto1b.repositorio.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class ProdutoService(
    val repository: ProdutoRepository
) {
    fun cadastrarProduto(produto: CaixaDaAgua): CaixaDaAgua {
        return repository.save(produto)
    }

    fun consultarProdutos() : List<CaixaDaAgua> {
        return repository.findAll()
    }
}