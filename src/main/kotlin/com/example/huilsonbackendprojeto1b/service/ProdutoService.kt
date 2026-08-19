package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.repositorio.ProdutoRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import java.sql.SQLException

@Service
class ProdutoService(
    val repository: ProdutoRepository,
) {
    fun salvarProduto(produto: CaixaDeAgua): CaixaDeAgua {
        return repository.save(produto)
    }

    fun listarTodos(): List<CaixaDeAgua>{
        return repository.findAll()
    }

    fun consultarPorStatus(status: String) : List<CaixaDeAgua> {
        return repository.findAllByStatus(status)
    }

    fun alterarStatus(id: Long, status: String) {
        try{
            repository.updateStatus(id, status)
            println("Produto ${if(status == "desativado") status else "reativado"}")
        } catch (e: SQLException) {
            println("ERRO: ${e.printStackTrace()}")
        }
    }
    
    fun alterarProduto(id: Long, produtoAtualizado: CaixaDeAgua): CaixaDeAgua {
        val produtoExistente = repository.findById(id)
            .orElseThrow { NoSuchElementException("Produto com ID $id não encontrado") }

        val produtoFinal = CaixaDeAgua(
            id = produtoExistente.id,
            marca = produtoAtualizado.marca,
            modelo = produtoAtualizado.modelo,
            dimensao = produtoAtualizado.dimensao,
            cor = produtoAtualizado.cor,
            material = produtoAtualizado.material,
            formato = produtoAtualizado.formato,
            fornecedor = produtoAtualizado.fornecedor,
            preco = produtoAtualizado.preco
        )

        return repository.save(produtoFinal)
    }
}