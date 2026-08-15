package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClienteRepository: JpaRepository<Cliente, Long>