package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.financeiro.Compra
import com.example.huilsonbackendprojeto1b.service.CompraService
import com.example.huilsonbackendprojeto1b.service.FuncionarioService
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import com.example.huilsonbackendprojeto1b.utils.validarCampoString

class CompraHandler(
    private val compraService: CompraService,
    private val produtoService: ProdutoService,
    private val funcionarioService: FuncionarioService
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Realizar Compra" to { realizarCompra() },
    )

    private fun realizarCompra(){

        val funcionarios = funcionarioService.consultarPorCargo(Cargo.FINANCEIRO)

        if(funcionarios.isEmpty()){
            print("Não há funcionários com autorização para realizar compras")
            return
        }

        println("----<| Funcionários |>----")
        funcionarios.forEach { funcionario ->
            funcionario.valores()
        }

        var idFuncionario: Long = 0
        do{
            idFuncionario = validarCampoNumerico("Insira o ID do funcionário responsável: ", tipo=1).toLong()
            if (funcionarios.none{ it.id == idFuncionario }) {
                println("ID inválido")
                continue
            }
            break
        } while(true)

        val descricao = validarCampoString("Insira a descrição: ", aceitarBranco = true)

        val compra = Compra(
            requisitor = funcionarios.first { it.id == idFuncionario },
            descricao = descricao,
        )

        var produtos = produtoService.listarProdutos()

        if (produtos.isEmpty()) {
            print("Não há produtos cadastrados, deseja cadastrar?(S/N): ")
            when (readln().uppercase()) {
                "S" -> {
                    ProdutoHandler(produtoService).cadastrarProduto()
                    produtos = produtoService.listarProdutos()
                }
                "N" -> {
                    println("Retornando")
                    return
                }
                else -> {
                    println("Opção inválida, retornando")
                    return
                }
            }
        }

        do {
            println("----<| Produtos |>----")
            produtos.forEach { produto ->
                produto.valores()
            }

            val idProduto = validarCampoNumerico("Insira o ID do produto a ser comprado ou deixe em branco para avançar com a compra: ", tipo = 1, aceitarBranco = true, nonIntProof = "-1").toLong()
            if(idProduto == -1L){
                if(compra.itensCompra.isEmpty()){
                    println("A compra precisa ter pelo menos um produto")
                    continue
                }
                break
            }
            if (produtos.none{ it.id == idProduto }) {
                println("ID inválido")
                continue
            }
            val quantidade = validarCampoNumerico("Digite a quantidade recebida: ", tipo = 1).toInt()
            compra.adicionarItem(produtos.first { it.id == idProduto },quantidade)
        } while (true)

        compraService.realizarCompra(compra)
        if(compra.id != null){
            compra.valores()
        }

    }
}