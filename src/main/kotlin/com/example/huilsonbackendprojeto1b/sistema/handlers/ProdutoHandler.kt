package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import com.example.huilsonbackendprojeto1b.utils.userInput
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import org.springframework.stereotype.Component

@Component
class ProdutoHandler(
    private val produtoService: ProdutoService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        // Reescreve a função do handler com as opções disponíveis
        "Cadastrar" to { cadastrarProduto() },
        "Consultar" to { consultarStatus("ativo") },
        "Consultar desativados" to { consultarStatus("desativado") },
        "Alterar" to { alterar() },
        "Desativar" to { alterarStatus("desativar") },
        "Reativar" to { alterarStatus("ativar") },
    )

    private fun cadastrarProduto() {
        val marca = userInput("Digite a marca: ")

        val modelo = userInput("Digite o modelo: ")

        val altura = validarCampoNumerico("Digite a altura: ").toDouble()

        val largura = validarCampoNumerico("Digita a largura: ").toDouble()

        val profundidade = validarCampoNumerico("Digita a profundidade: ").toDouble()

        val dimensao: MutableList<Double> = mutableListOf(altura, largura, profundidade)

        var cor: Cor
        Cor.entries.forEach { c ->
            println("${c.ordinal} - ${c.name.replace("_", " ")}")
        }
        do {
            val codigoCor = validarCampoNumerico("Escolha a cor: ", aceitarDecimal = false).toInt()
            if (codigoCor !in Cor.entries.indices) {
                println("Código da cor não existe")
                continue
            }
            cor = Cor.entries[codigoCor]
            break
        } while (true)

        var material: Material
        Material.entries.forEach { m ->
            println("${m.ordinal} - ${m.name.replace("_", " ")}")
        }
        do {
            val codigoMaterial = validarCampoNumerico("Digite o material: ", aceitarDecimal = false).toInt()
            if (codigoMaterial !in Material.entries.indices) {
                println("Código do material não existe")
                continue
            }
            material = Material.entries[codigoMaterial]
            break
        } while (true)

        var formato: Formatos
        Formatos.entries.forEach { f ->
            println("${f.ordinal} - ${f.name.replace("_", " ")}")
        }
        do {
            val codigoFormato = validarCampoNumerico("Escolha o formato: ", aceitarDecimal = false).toInt()
            if (codigoFormato !in Formatos.entries.indices) {
                println("Código do formato não existe")
                continue
            }
            formato = Formatos.entries[codigoFormato]
            break
        } while (true)

        val fornecedor = userInput("Digite o fornecedor: ")

        val preco = validarCampoNumerico("Digite o preço: ").toBigDecimal()

        val produto = CaixaDeAgua(
            marca = marca,
            modelo = modelo,
            dimensao = dimensao,
            cor = cor,
            material = material,
            formato = formato,
            fornecedor = fornecedor,
            preco = preco.toString()
        )

        try {
            produtoService.salvarProduto(produto)
        } catch (e: Exception) {
            println("Erro ao cadastrar produto: $e")
        }
        println("Cadastrado: ${produto.valores()}")
    }

    private fun consultarStatus(tipo: String) {
        val produtos = produtoService.consultarPorStatus(tipo)
        if (produtos.isEmpty()) {
            if (tipo == "ativo") {
                print("Não há produtos ativos, deseja cadastrar?(S/N): ")
                when (readln().uppercase()) {
                    "S" -> cadastrarProduto()
                    "N" -> println("Retornando")
                    else -> println("Opção inválida, retornando")
                }
            } else if (tipo == "desativado") {
                print("Não há produtos desativados")
            }
            return
        }
        produtos.forEach { produto ->
            println(produto.valores())
        }
    }

    private fun alterar() {
        val produtos = produtoService.listarTodos()
        val IDs: MutableList<Long?> = mutableListOf()

        if (produtos.isEmpty()) {
            println("Não há produtos cadastrados")
            return
        }

        println("----<| Produtos cadastrados |>----")
        produtos.forEach { produto ->
            IDs.add(produto.id)
            println(produto.valores())
        }

        var idProduto: Long
        do {
            idProduto = validarCampoNumerico("Insira o ID do produto a ser alterado: ", aceitarDecimal = false).toLong()
            if (idProduto !in IDs) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        val produtoAtual = produtos.first { it.id == idProduto }

        println()
        println("Deixe em branco para manter o valor atual.")

        print("Digite a marca (atual: ${produtoAtual.marca}): ")
        val marcaInput = readln()
        val marca = if (marcaInput.isBlank()) produtoAtual.marca else marcaInput

        print("Digite o modelo (atual: ${produtoAtual.modelo}): ")
        val modeloInput = readln()
        val modelo = if (modeloInput.isBlank()) produtoAtual.modelo else modeloInput

        val alturaInput = validarCampoNumerico("Digite a altura (atual: ${produtoAtual.dimensao[0]}): ", aceitarBranco = true)
        val altura = if (alturaInput.isBlank()) produtoAtual.dimensao[0]
        else alturaInput.replace(",", ".").toDouble()

        val larguraInput = validarCampoNumerico("Digita a largura (atual: ${produtoAtual.dimensao[1]}): ", aceitarBranco = true)
        val largura = if (larguraInput.isBlank()) produtoAtual.dimensao[1]
        else larguraInput.replace(",", ".").toDouble()

        val profundidadeInput = validarCampoNumerico("Digita a profundidade (atual: ${produtoAtual.dimensao[2]}): ", aceitarBranco = true)
        val profundidade = if (profundidadeInput.isBlank()) produtoAtual.dimensao[2]
        else profundidadeInput.replace(",", ".").toDouble()

        val dimensao: MutableList<Double> = mutableListOf(altura, largura, profundidade)

        var cor: Cor = produtoAtual.cor
        Cor.entries.forEach { c ->
            println("${c.ordinal} - ${c.name.replace("_", " ")}")
        }
        do {
            val corInput = validarCampoNumerico("Escolha a cor (atual: ${produtoAtual.cor}) — deixe em branco para manter: ", aceitarBranco = true, aceitarDecimal = false)
            if (corInput.isBlank()) {
                break
            }
            val codigoCor = corInput.toInt()
            if (codigoCor !in Cor.entries.indices) {
                println("Código da cor não existe")
                continue
            }
            cor = Cor.entries[codigoCor]
            break
        } while (true)

        var material: Material = produtoAtual.material
        Material.entries.forEach { m ->
            println("${m.ordinal} - ${m.name.replace("_", " ")}")
        }
        do {
            val materialInput = validarCampoNumerico("Escolha o material (atual: ${produtoAtual.material}) — deixe em branco para manter: ", aceitarBranco = true, aceitarDecimal = false)
            if (materialInput.isBlank()) {
                break
            }
            val codigoMaterial = materialInput.toInt()
            if (codigoMaterial !in Material.entries.indices) {
                println("Código do material não existe")
                continue
            }
            material = Material.entries[codigoMaterial]
            break
        } while (true)

        var formato: Formatos = produtoAtual.formato
        Formatos.entries.forEach { f ->
            println("${f.ordinal} - ${f.name.replace("_", " ")}")
        }
        do {
            val formatoInput = validarCampoNumerico("Escolha o formato (atual: ${produtoAtual.formato}) — deixe em branco para manter: ", aceitarBranco = true, aceitarDecimal = false)
            if (formatoInput.isBlank()) {
                break
            }
            val codigoFormato = formatoInput.toInt()
            if (codigoFormato !in Formatos.entries.indices) {
                println("Código do formato não existe")
                continue
            }
            formato = Formatos.entries[codigoFormato]
            break
        } while (true)

        print("Digite o fornecedor (atual: ${produtoAtual.fornecedor}): ")
        val fornecedorInput = readln()
        val fornecedor = if (fornecedorInput.isBlank()) produtoAtual.fornecedor else fornecedorInput

        val precoInput = validarCampoNumerico("Digite o preço (atual: ${produtoAtual.preco}): ", aceitarBranco = true)
        val preco = if (precoInput.isBlank()) produtoAtual.preco
        else precoInput.replace(",", ".").toBigDecimal().toString()

        val produtoAtualizado = CaixaDeAgua(
            id = produtoAtual.id,
            marca = marca,
            modelo = modelo,
            dimensao = dimensao,
            cor = cor,
            material = material,
            formato = formato,
            fornecedor = fornecedor,
            preco = preco
        )

        try {
            produtoService.alterarProduto(idProduto, produtoAtualizado)
            println("Produto atualizado: ${produtoAtualizado.valores()}")
        } catch (e: Exception) {
            println("Erro ao atualizar produto: $e")
        }
    }

    private fun alterarStatus(tipo: String) {
        val stringTipo = if (tipo == "ativar") "ativo" else "desativado"
        val stringConsulta = if (tipo == "ativar") "desativado" else "ativo"
        println(stringTipo)
        val produtos = produtoService.consultarPorStatus(stringConsulta)
        val IDs: MutableList<Long?> = mutableListOf()

        if (produtos.isEmpty()) {
            print("Não há produtos ${stringConsulta}s")
            return
        }

        println("----<| Produtos ${stringConsulta}s |>----")
        produtos.forEach { produto ->
            IDs.add(produto.id)
            println(produto.valores())
        }

        var idProduto: Long = 0
        do {
            print("Insira o ID do produto a ser $stringTipo: ")
            idProduto = readln().toLong()
            if (idProduto !in IDs) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        produtoService.alterarStatus(idProduto, stringTipo)
    }
}