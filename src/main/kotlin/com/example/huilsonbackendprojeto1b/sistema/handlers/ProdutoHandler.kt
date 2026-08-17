package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import org.springframework.stereotype.Component
import sun.jvm.hotspot.HelloWorld.e

@Component
class ProdutoHandler(
    private val produtoService: ProdutoService
): CrudHandler {
    override fun cadastrar() {
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
            produtoService.cadastrarProduto(produto)
        } catch (e : Exception){
            println("Erro ao cadastrar produto: $e")
        }
        println("Cadastrado: ${produto.valores()}")
    }

    override fun consultar() {
        val produtos = produtoService.consultarProdutos()
        if(produtos.isEmpty()){
            print("Não há produtos cadastrados, deseja cadastrar?(S/N): ")
            when(readln().uppercase()){
                "S" -> cadastrar()
                "N" -> println("Retornando")
                else -> println("Opção inválida, retornando")
            }
            return
        }
        produtos.forEach { produto ->
            println(produto.valores())
        }
    }
    override fun alterar() {
        println("Produto alterado")
    }
    override fun excluir() {
        println("Produto excluido")
    }

}