package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.repositorio.ProdutoRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service

@Service
class ProdutoService(
    val repository: ProdutoRepository
) {
    fun cadastrarProduto(produto: CaixaDeAgua): CaixaDeAgua {
        return repository.save(produto)
    }

    fun consultarAtivos() : List<CaixaDeAgua> {
        return repository.findAll()
    }

    @Modifying
    @Query("UPDATE caixa_de_agua c SET c.status = 'desativado' WHERE c.id = :id")
    fun desativarProduto(@Param("id") id: Long){
        println("Produto desativado")
    }
}