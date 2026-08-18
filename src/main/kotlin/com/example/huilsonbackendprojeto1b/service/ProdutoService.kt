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
    val repository: ProdutoRepository
) {
    fun salvarProduto(produto: CaixaDeAgua): CaixaDeAgua {
        return repository.save(produto)
    }

    fun consultarPorStatus(status: String) : List<CaixaDeAgua> {
        return repository.findAllByStatus(status)
    }

    fun alterarStatus(id: Long, status: String) {
        try{
            repository.updateStatus(id, status)
            println("Produto desativado")
        } catch (e: SQLException) {
            println("ERRO: ${e.printStackTrace()}")
        }
    }
}