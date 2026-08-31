package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.repositorio.JPAProduto
import java.sql.SQLException

class ProdutoService {
    private val jpaProduto = JPAProduto()

    fun salvarProduto(produto: CaixaDeAgua): CaixaDeAgua {
        return jpaProduto.salvar(produto)
    }

    fun listarProdutos(): List<CaixaDeAgua> {
        return jpaProduto.listar()
    }

    fun consultarPorStatus(status: String): List<CaixaDeAgua> {
        return jpaProduto.consultarPorStatus(status)
    }

    fun alterarStatus(id: Long, status: String) {
        try {
            jpaProduto.alterarCampo(id, "status", status)
            println("Produto ${if (status == "desativado") status else "reativado"}")
        } catch (e: SQLException) {
            println("ERRO: ${e.printStackTrace()}")
        }
    }

    fun alterarProduto(id: Long, produtoAtualizado: CaixaDeAgua): CaixaDeAgua {
        val produtos = jpaProduto.listar()
        if (produtos.none { it.id == id }) {
            throw NoSuchElementException("Produto com ID $id não encontrado")
        }
        return jpaProduto.editar(produtoAtualizado, id)
    }
}
