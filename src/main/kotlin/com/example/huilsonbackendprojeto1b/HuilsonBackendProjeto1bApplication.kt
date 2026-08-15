package com.example.huilsonbackendprojeto1b

import com.example.huilsonbackendprojeto1b.enumeradores.OpcoesMenu
import com.example.huilsonbackendprojeto1b.sistema.handlers.ProdutoHandler
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import com.example.huilsonbackendprojeto1b.sistema.mainMenu

@SpringBootApplication
class HuilsonBackendProjeto1bApplication

fun main(args: Array<String>) {
    runApplication<HuilsonBackendProjeto1bApplication>(*args)
}

@Component
class TerminalRunner( // Só roda depois que o Spring termina as configurações, criações e etc
    private val produtoHandler: ProdutoHandler,
) : CommandLineRunner {

    override fun run(vararg args: String) {
        val handlers = mapOf(
            OpcoesMenu.PRODUTO to produtoHandler,
        )
        mainMenu(handlers)
    }
}
