package com.example.huilsonbackendprojeto1b.sistema

import com.example.huilsonbackendprojeto1b.enumeradores.OpcoesMenu
import com.example.huilsonbackendprojeto1b.enumeradores.OpcoesSubMenu
import com.example.huilsonbackendprojeto1b.sistema.handlers.CrudHandler

fun mainMenu(handlers: Map<OpcoesMenu, CrudHandler>) {
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

        println()
        println("----<| ${OpcoesMenu.entries[opcao]} |>----")
        OpcoesSubMenu.entries.forEachIndexed { i, it ->
            println("${i} - $it")
        }
        do {
            print("Insira a opção: ")
            subOpcao = readln().toInt()
            if(subOpcao !in OpcoesSubMenu.entries.indices){
                println("Opção incorreta")
                continue
            }
            break
        }while (true)

        if(OpcoesSubMenu.entries[subOpcao] == OpcoesSubMenu.VOLTAR){
            continue
        }

        val handler = handlers[OpcoesMenu.entries[opcao]] ?: continue
        when(OpcoesSubMenu.entries[subOpcao]){
            OpcoesSubMenu.VOLTAR -> continue
            OpcoesSubMenu.CADASTRAR -> handler.cadastrar()
            OpcoesSubMenu.CONSULTAR -> handler.consultar()
            OpcoesSubMenu.ALTERAR -> handler.alterar()
            OpcoesSubMenu.EXCLUIR -> handler.excluir()
        }

    } while (true)
}
