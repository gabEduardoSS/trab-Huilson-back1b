package com.example.huilsonbackendprojeto1b.sistema

import com.example.huilsonbackendprojeto1b.enumeradores.OpcoesMenu
import com.example.huilsonbackendprojeto1b.sistema.handlers.OpcoesHandler

fun mainMenu(handlers: Map<OpcoesMenu, OpcoesHandler>) {
    do {
        var opcao = 0;
        var subOpcao = 0;

        println()
        OpcoesMenu.entries.forEachIndexed { i, it ->
            println("${i} - $it")
        }

        do {
            print("Insira a opção: ")
            opcao = readln().toInt()
            if(opcao !in OpcoesMenu.entries.indices){
                println("Opção incorreta")
                continue
            }
            break
        }while (true)

        if(OpcoesMenu.entries[opcao] == OpcoesMenu.SAIR){
            break
        }

        val handler = handlers[OpcoesMenu.entries[opcao]] ?: continue
        val opcoesHandler = handler.opcoes()

        println()
        println("----<| ${OpcoesMenu.entries[opcao]} |>----")
        println("0 - Voltar")

        opcoesHandler.forEachIndexed { index, (texto, func) ->
            println("${index+1} - $texto")
        }

        do{
            print("Insira a opção: ")
            subOpcao = readln().toInt()
            if(opcao !in 0..<opcoesHandler.size){
                println("Opção inválida")
                continue
            }
            break
        } while(true)

        if(subOpcao == 0) continue

        opcoesHandler[subOpcao-1].second.invoke() // Chama a função da opção específica, sendo ela o segundo elemento do pair

    } while (true)
}
