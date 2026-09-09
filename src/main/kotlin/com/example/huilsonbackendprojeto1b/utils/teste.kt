package com.example.huilsonbackendprojeto1b.utils

import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import com.example.huilsonbackendprojeto1b.repositorio.JPAConexao
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import java.sql.Connection

fun main(){
    val teste = validarCampoNumerico("Teste: ", tipo = 1, aceitarBranco = true).toInt()
}