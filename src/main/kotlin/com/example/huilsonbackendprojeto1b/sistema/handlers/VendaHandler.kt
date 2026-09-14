package com.example.huilsonbackendprojeto1b.sistema.handlers

import com.example.huilsonbackendprojeto1b.enumeradores.Cargo
import com.example.huilsonbackendprojeto1b.financeiro.Venda
import com.example.huilsonbackendprojeto1b.service.ClienteService
import com.example.huilsonbackendprojeto1b.service.FuncionarioService
import com.example.huilsonbackendprojeto1b.service.ProdutoService
import com.example.huilsonbackendprojeto1b.service.VendaService
import com.example.huilsonbackendprojeto1b.utils.validarCampoNumerico
import com.example.huilsonbackendprojeto1b.utils.validarCampoString

class VendaHandler(
    private val vendaService: VendaService,
    private val funcionarioService: FuncionarioService,
    private val produtoService: ProdutoService,
    private val clienteService: ClienteService,
) : OpcoesHandler {
    override fun opcoes(): List<Pair<String, () -> Unit>> = listOf(
        "Realizar Venda" to { realizarVenda() },
        )

    fun realizarVenda() {
        var funcionarios = funcionarioService.consultarPorCargo(Cargo.ATENDIMENTO)
        var clientes = clienteService.listarClientes()

        if (funcionarios.isEmpty()) {
            print("Não há funcionários com autorização para realizar vendas cadastrados, deseja cadastrar?(S/N): ")
            when (readln().uppercase()) {
                "S" -> {
                    FuncionarioHandler(funcionarioService).cadastrarFuncionario(Cargo.ATENDIMENTO)
                    funcionarios = funcionarioService.consultarPorCargo(Cargo.ATENDIMENTO)
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

        println("----<| Funcionários |>----")
        funcionarios.forEach { funcionario ->
            funcionario.valores()
        }

        var idFuncionario: Long = 0
        do {
            print("Insira o ID do funcionário responsável: ")
            idFuncionario = readln().toLong()
            if (funcionarios.none { it.id == idFuncionario }) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        if (clientes.isEmpty()) {
            print("Não há clientes cadastrados, deseja cadastrar?(S/N): ")
            when (readln().uppercase()) {
                "S" -> {
                    ClienteHandler(clienteService).cadastrarCliente()
                    clientes = clienteService.listarClientes()
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

        println("----<| Clientes |>----")
        clientes.forEach { cliente ->
            println(cliente.valores())
        }

        var idCliente: Long = 0
        do {
            idCliente = validarCampoNumerico("Insira o ID do cliente: ", tipo=1).toLong()
            if (clientes.none { it.id == idCliente }) {
                println("ID inválido")
                continue
            }
            break
        } while (true)

        val descricao = validarCampoString("Insira a descrição: ", aceitarBranco = true)

        val venda = Venda(
            vendedor = funcionarios.first { it.id == idFuncionario },
            cliente = clientes.first { it.id == idCliente },
            descricao = descricao,
        )

        val produtos = produtoService.consultarPorStatus("ativo")

        if (produtos.isEmpty()) {
            print("Não há produtos ativos")
            return
        }

        do {
            println("----<| Produtos |>----")
            produtos.forEach { produto ->
                println(produto.valores())
            }

            val idProduto = validarCampoNumerico(
                "Insira o ID do produto a ser vendido ou deixe em branco para avançar com a venda: ",
                tipo = 1,
                aceitarBranco = true,
                nonIntProof = "-1"
            ).toLong()

            if (idProduto == -1L) {
                if (venda.itensVenda.isEmpty()) {
                    println("A venda precisa ter pelo menos um produto")
                    continue
                }
                break
            }
            if (produtos.none { it.id == idProduto }) {
                println("ID inválido")
                continue
            }

            val quantidade = validarCampoNumerico("Digite a quantidade a ser vendida: ", tipo = 1).toInt()

            venda.adicionarItem(produtos.first { it.id == idProduto }, quantidade)
        } while (true)


        vendaService.realizarVenda(venda)
        if (venda.status != null) {
            venda.valores()
        }
    }
}