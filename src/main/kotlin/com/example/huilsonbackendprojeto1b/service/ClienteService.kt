package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.repositorio.ClienteRepository
import org.springframework.stereotype.Service


@Service
class ClienteService(
    val repository: ClienteRepository
) {
    fun salvarCliente(cliente: Cliente): Cliente {
        return repository.save(cliente)
    }
}