package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import org.springframework.stereotype.Component

@Component
class ProdutoHandler(
    private val produtoService: ProdutoService
): OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf( // Reescreve a função do handler com as opções disponíveis
        "Cadastrar" to ::cadastrar,
        "Consultar" to {consultarStatus("ativo")},
        "Consultar desativados" to {consultarStatus("desativado")},
        "Alterar" to ::alterar,
        "Desativar" to {alterarStatus("desativar")},
        "Reativar" to {alterarStatus("ativar")},
    )

    private fun cadastrar() {
        print("Digite a marca: ")
        val marca = readln()

        print("Digite o modelo: ")
        val modelo = readln()

        print("Digite a altura: ")
        val altura = readln().replace(",", ".").toDouble()

        print("Digita a largura: ")
        val largura = readln().replace(",", ".").toDouble()

        print("Digita a profundidade: ")
        val profundidade = readln().replace(",", ".").toDouble()

        val dimensao: MutableList<Double> = mutableListOf(altura, largura, profundidade)

        println("Escolha a cor: ")
        var cor: Cor
        Cor.entries.forEach { c ->
            println("${c.ordinal} - ${c.name.replace("_", " ")}")
        }
        do {
            val codigoCor = readln().toInt()
            if(codigoCor !in Cor.entries.indices){
                println("Código da cor não existe")
                continue
            }
            cor = Cor.entries[codigoCor]
            break
        } while(true)

        println("Escolha o material: ")
        var material : Material
        Material.entries.forEach { m ->
            println("${m.ordinal} - ${m.name.replace("_", " ")}")
        }
        do {
            val codigoMaterial = readln().toInt()
            if(codigoMaterial !in Material.entries.indices){
                println("Código do material não existe")
                continue
            }
            material = Material.entries[codigoMaterial]
            break
        } while(true)

        println("Escolha o formato: ")
        var formato : Formatos
        Formatos.entries.forEach { f ->
            println("${f.ordinal} - ${f.name.replace("_", " ")}")
        }
        do {
            val codigoFormato = readln().toInt()
            if(codigoFormato !in Formatos.entries.indices){
                println("Código do formato não existe")
                continue
            }
            formato = Formatos.entries[codigoFormato]
            break
        } while(true)

        print("Digite o fornecedor: ")
        val fornecedor = readln()

        print("Digite o preço: ")
        val preco = readln().replace(",", ".").toBigDecimal()

        val produto: CaixaDeAgua = CaixaDeAgua(
            marca = marca,
            modelo = modelo,
            dimensao = dimensao,
            cor = cor,
            material = material,
            formato = formato,
            fornecedor = fornecedor,
            preco = preco.toString()
        )

        try{
            produtoService.salvarProduto(produto)
        } catch (e : Exception){
            println("Erro ao cadastrar produto: $e")
        }
        println("Cadastrado: ${produto.valores()}")
    }

    private fun consultarStatus(tipo: String) {
        val produtos = produtoService.consultarPorStatus(tipo)
        if(produtos.isEmpty()){
            if(tipo == "ativo") {
                print("Não há produtos ativos, deseja cadastrar?(S/N): ")
                when(readln().uppercase()){
                    "S" -> cadastrar()
                    "N" -> println("Retornando")
                    else -> println("Opção inválida, retornando")
                }
            } else if(tipo == "desativado"){
                print("Não há produtos desativados")
            }
            return
        }
        produtos.forEach { produto ->
            println(produto.valores())
        }
    }

    private fun alterar() {
        println("Produto alterado")
    }
    private fun alterarStatus(tipo: String) {
        val stringTipo = if(tipo == "ativar") "ativo" else "desativado"
        val stringConsulta = if(tipo == "ativar") "desativado" else "ativo"
        println(stringTipo)
        val produtos = produtoService.consultarPorStatus(stringConsulta)
        val IDs: MutableList<Long?> = mutableListOf()

        if(produtos.isEmpty()){
            print("Não há produtos ${stringConsulta}s")
            return
        }

        println("----<| Produtos ${stringConsulta}s |>----")
        produtos.forEach { produto ->
            IDs.add(produto.id)
            println(produto.valores())
        }

        var idProduto: Long = 0
        do{
            print("Insira o ID do produto a ser $stringTipo: ")
            idProduto = readln().toLong()
            if(idProduto !in IDs){
                println("ID inválido")
                continue
            }
            break
        } while(true)

        produtoService.alterarStatus(idProduto, stringTipo)
    }
}