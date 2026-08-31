package com.example.huilsonbackendprojeto1b.sistema.handlers

interface OpcoesHandler { // Cria uma função padrão, em branco, para ser alterada depois
    fun opcoes(): List<Pair<String, () -> Unit>> // vai aceitar uma lista, cada elemento sendo um par contendo uma string e uma função
}