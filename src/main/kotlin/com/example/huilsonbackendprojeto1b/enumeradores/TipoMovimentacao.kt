package com.example.huilsonbackendprojeto1b.enumeradores

enum class TipoMovimentacao {
    ENTRADA, SAIDA;

    fun getString() : String{
        return when (this) {
            ENTRADA -> "Entrada"
            SAIDA -> "Saida"
        }
    }
}