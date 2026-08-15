package com.example.huilsonbackendprojeto1b.controller

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.produto.CaixaDaAgua
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import org.springframework.stereotype.Controller

class ProdutoController(
    val service: ProdutoService
) {
}