package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository : JpaRepository<CaixaDeAgua, Long>