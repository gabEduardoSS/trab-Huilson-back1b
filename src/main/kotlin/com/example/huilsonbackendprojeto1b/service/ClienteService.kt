package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.pessoas.Cliente
import com.example.huilsonbackendprojeto1b.repositorio.JPACliente

class ClienteService {
    private val jpaCliente = JPACliente()

    fun salvarCliente(cliente: Cliente): Cliente {
        return jpaCliente.salvar(cliente)
    }

    fun listarClientes(): List<Cliente> {
        return jpaCliente.listar()
    }

    fun alterarCliente(id: Long, clienteAtualizado: Cliente): Cliente {
        val clientes = jpaCliente.listar()
        if (clientes.none { it.id == id }) {
            throw NoSuchElementException("Cliente com o ID ${id} não encontrado")
        }
        return jpaCliente.editar(clienteAtualizado, id)
    }
}
