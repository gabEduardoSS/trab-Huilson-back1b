package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.repositorio.JPAMovimentacao
import com.example.huilsonbackendprojeto1b.repositorio.JPATransacao
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import com.example.huilsonbackendprojeto1b.utils.validarCampoString

class ConsultasHandler(): OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Consultar Movimentações(simples)" to { consultarMovimentacoes() },
        "Consultar Movimentações(detalhado)" to { consultarMovimentacoes(detalhamento=2) },
        "Consultar Transações(simples)" to { consultarTransacoes() },
        "Consultar Transações(detalhado)" to { consultarTransacoes(detalhamento=2) }
    )


    /**
     * Detalhamento: Int:
     *  1 -> simples,
     *  2 -> detalhado,
     * */
    fun consultarMovimentacoes(detalhamento: Int = 1){
        val opt = validarCampoString("""
            Filtrar por tipo?
             - (E) -> Entradas,
             - (S) -> Saidas,
             - Qualquer outra coisa para não filtrar
             Opção: 
        """.trimIndent(), aceitarBranco = true).uppercase()

        var tipo: TipoMovimentacao? = null
        if(!opt.isBlank() && (opt == "E" || opt == "S")) {
            if(opt == "E"){
                tipo = TipoMovimentacao.ENTRADA
            } else{
                tipo = TipoMovimentacao.SAIDA
            }
        }

        val movimentacoes = JPAMovimentacao.consultarMovimentacao(tipo = tipo)

        if(movimentacoes.isEmpty()){
            print("Não há movimentações")
            if(tipo != null) print(" do tipo: ${tipo.name}")
            println()
            return
        }

        movimentacoes.forEach { movimentacao ->
            movimentacao.valores()
            if(detalhamento == 2) movimentacao.produto.valores()
        }
    }

    /**
     * Detalhamento: Int:
     *  1 -> simples,
     *  2 -> detalhado,
     * */
    fun consultarTransacoes(detalhamento: Int = 1){
        val opt = validarCampoString("""
            Filtrar por tipo?
             - (E) -> Entradas,
             - (S) -> Saidas,
             - Qualquer outra coisa para não filtrar
             Opção: 
        """.trimIndent(), aceitarBranco = true).uppercase()

        var tipo: TipoTransacao? = null
        if(!opt.isBlank() && (opt == "E" || opt == "S")) {
            if(opt == "E"){
                tipo = TipoTransacao.ENTRADA
            } else{
                tipo = TipoTransacao.SAIDA
            }
        }
        val transacoes = JPATransacao.consultarTransacao(tipo = tipo)

        if(transacoes.isEmpty()){
            print("Não há transações")
            if(tipo != null) print(" do tipo: ${tipo.name}")
            println()
            return
        }

        transacoes.forEach { transacao ->
            transacao.valores()
            if(detalhamento == 2) transacao.pessoa.valores()
        }
    }
}