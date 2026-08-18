package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository : JpaRepository<CaixaDeAgua, Long>{
    fun findAllByStatus(status: String): List<CaixaDeAgua>

    @Modifying
    @Transactional
    @Query("UPDATE CaixaDeAgua c SET c.status = :status WHERE c.id = :id")
    fun updateStatus(id: Long, status: String)
}