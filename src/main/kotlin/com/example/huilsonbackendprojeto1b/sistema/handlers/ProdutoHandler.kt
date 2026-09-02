package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import com.example.huilsonbackendprojeto1b.produto.Movimentacao
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import com.example.huilsonbackendprojeto1b.utils.validarCampoString
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico

class ProdutoHandler(
    private val produtoService: ProdutoService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        // Reescreve a função do handler com as opções disponíveis
        "Cadastrar" to { cadastrarProduto() },
        "Consultar" to { consultarStatus("ativo") },
        "Consultar desativados" to { consultarStatus("desativado") },
        "Alterar" to { alterarProduto() },
        "Desativar" to { alterarStatus("desativar") },
        "Reativar" to { alterarStatus("ativar") },
        "Recebimento" to { recebimento() }
    )

    private fun cadastrarProduto() {
        val marca = validarCampoString("Digite a marca: ")

        val modelo = validarCampoString("Digite o modelo: ")

        val altura = validarCampoNumerico("Digite a altura: ").toDouble()

        val largura = validarCampoNumerico("Digita a largura: ").toDouble()

        val profundidade = validarCampoNumerico("Digita a profundidade: ").toDouble()

        val dimensao: MutableList<Double> = mutableListOf(altura, largura, profundidade)

        var cor: Cor
        Cor.entries.forEach { c ->
            println("${c.ordinal} - ${c.name.replace("_", " ")}")
        }
        do {
            val codigo = validarCampoNumerico("Escolha a cor: ", tipo = 1).toInt()
            if (codigo !in Cor.entries.indices) {
                println("Código da cor não existe")
                continue
            }
            cor = Cor.entries[codigo]
            break
        } while (true)

        var material: Material
        Material.entries.forEach { m ->
            println("${m.ordinal} - ${m.name.replace("_", " ")}")
        }
        do {
            val codigoMaterial = validarCampoNumerico("Digite o material: ", tipo = 1).toInt()
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
            val codigoFormato = validarCampoNumerico("Escolha o formato: ", tipo = 1).toInt()
            if (codigoFormato !in Formatos.entries.indices) {
                println("Código do formato não existe")
                continue
            }
            formato = Formatos.entries[codigoFormato]
            break
        } while (true)

        val fornecedor = validarCampoString("Digite o fornecedor: ")

        val preco = validarCampoNumerico("Digite o preço: ").toBigDecimal()

        val produto = CaixaDeAgua(
            marca = marca,
            modelo = modelo,
            dimensao = dimensao,
            cor = cor,
            material = material,
            formato = formato,
            fornecedor = fornecedor,
            preco = preco,
        )

        try {
            produtoService.salvarProduto(produto)
        } catch (e: Exception) {
            println("Erro ao cadastrar produto: $e")
        }
        println("Cadastrado: ${produto.valores()}")
    }

    private fun consultarStatus(status: String) {
        val produtos = produtoService.consultarPorStatus(status)
        if (produtos.isEmpty()) {
            if (status == "ativo") {
                print("Não há produtos ativos, deseja cadastrar?(S/N): ")
                when (readln().uppercase()) {
                    "S" -> cadastrarProduto()
                    "N" -> println("Retornando")
                    else -> println("Opção inválida, retornando")
                }
            } else if (status == "desativado") {
                print("Não há produtos desativados")
            }
            return
        }
        produtos.forEach { produto ->
            println(produto.valores())
        }
    }

    private fun alterarProduto() {
        val produtos = produtoService.listarProdutos()

        if (produtos.isEmpty()) {
            println("Não há produtos cadastrados")
            return
        }

        println("----<| Produtos cadastrados |>----")
        produtos.forEach { produto ->
            println(produto.valores())
        }

        var idProduto: Long = 0
        do {
            idProduto = validarCampoNumerico("Insira o ID do produto a ser alterado: ", tipo = 1).toLong()
            if (produtos.none{ it.id == idProduto }) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        val produtoAtual = produtos.first { it.id == idProduto }

        println()
        println("Deixe em branco para manter o valor atual: ")

        val marcaInput = validarCampoString("Digite a marca (atual: ${produtoAtual.marca}): ", aceitarBranco = true)
        val marca = marcaInput.ifBlank { produtoAtual.marca }

        val modeloInput = validarCampoString("Digite o modelo (atual: ${produtoAtual.modelo}): ", aceitarBranco = true)
        val modelo = modeloInput.ifBlank { produtoAtual.modelo }

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
            val corInput = validarCampoNumerico("Escolha a cor (atual: ${produtoAtual.cor}) — deixe em branco para manter: ", aceitarBranco = true, tipo = 1)
            if (corInput.isBlank()) {
                break
            }
            val codigo = corInput.toInt()
            if (codigo !in Cor.entries.indices) {
                println("Código da cor não existe")
                continue
            }
            cor = Cor.entries[codigo]
            break
        } while (true)

        var material: Material = produtoAtual.material
        Material.entries.forEach { m ->
            println("${m.ordinal} - ${m.name.replace("_", " ")}")
        }
        do {
            val materialInput = validarCampoNumerico("Escolha o material (atual: ${produtoAtual.material}) — deixe em branco para manter: ", aceitarBranco = true, tipo = 1)
            if (materialInput.isBlank()) {
                break
            }
            val codigo = materialInput.toInt()
            if (codigo !in Material.entries.indices) {
                println("Código do material não existe")
                continue
            }
            material = Material.entries[codigo]
            break
        } while (true)

        var formato: Formatos = produtoAtual.formato
        Formatos.entries.forEach { f ->
            println("${f.ordinal} - ${f.name.replace("_", " ")}")
        }
        do {
            val formatoInput = validarCampoNumerico("Escolha o formato (atual: ${produtoAtual.formato}) — deixe em branco para manter: ", aceitarBranco = true, tipo = 1)
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

        val fornecedorInput = validarCampoString("Digite o fornecedor (atual: ${produtoAtual.fornecedor}): ", aceitarBranco = true)
        val fornecedor = fornecedorInput.ifBlank { produtoAtual.fornecedor }

        val precoInput = validarCampoNumerico("Digite o preço (atual: ${produtoAtual.preco}): ", aceitarBranco = true)
        val preco = if (precoInput.isBlank()) produtoAtual.preco
        else precoInput.replace(",", ".").toBigDecimal()

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
        val status = if (tipo == "ativar") "ativo" else "desativado"
        val stringConsulta = if (tipo == "ativar") "desativado" else "ativo"
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
            print("Insira o ID do produto a ser $status: ")
            idProduto = readln().toLong()
            if (idProduto !in IDs) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        produtoService.alterarStatus(idProduto, status)
    }

    private fun recebimento(){
        val produtos = produtoService.listarProdutos()
        val IDs: MutableList<Pair<Int, Long?>> = mutableListOf()

        if (produtos.isEmpty()) {
            print("Não há produtos cadastrados")
            return
        }

        println("----<| Produtos |>----")
        produtos.forEachIndexed { idx, produto ->
            IDs.add(idx to produto.id)
            println(produto.valores())
        }

        var idProduto: Long = 0
        do {
            print("Insira o ID do produto recebido: ")
            idProduto = readln().toLong()
            if (!IDs.any{it.second == idProduto}) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        val quantidade = validarCampoNumerico("Digite a quantidade recebida: ", tipo = 1).toInt()

        val movimentacao = Movimentacao(
            produto = produtos[IDs.find { it.second == idProduto  }!!.first],
            quantidade = quantidade,
            tipo = TipoMovimentacao.ENTRADA
        )

        movimentacao.movimentar()
    }
}