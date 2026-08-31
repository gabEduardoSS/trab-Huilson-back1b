package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formatos
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class JPAProduto(
    var c: Connection? = null
) {
    fun salvar(a: CaixaDeAgua): CaixaDeAgua {
        println("Salvando...")
        try {
            c = JPAConexao().conectar()
            val sql = "INSERT INTO caixa_de_agua " +
                    "(marca, modelo, dimensao, cor, material, formato, fornecedor, preco, quantidade, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, ?)"

            val stmt = c!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            val doublePrecision = c!!.createArrayOf("float8", a.dimensao.toTypedArray())

            stmt.setString(1, a.marca)
            stmt.setString(2, a.modelo)
            stmt.setArray(3, doublePrecision)
            stmt.setString(4, a.cor.name)
            stmt.setString(5, a.material.name)
            stmt.setString(6, a.formato.name)
            stmt.setString(7, a.fornecedor)
            stmt.setBigDecimal(8, a.preco)
            stmt.setString(9, a.status)

            stmt.executeUpdate()

            val chaves = stmt.generatedKeys
            if (chaves.next()) {
                a.id = chaves.getLong(1)
            }

            stmt.close()
        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
        } finally {
            c?.close() // antes só fechava se não desse exceção; agora é garantido
        }
        return a
    }

    fun listar(): List<CaixaDeAgua> {
        return listarComFiltroDeStatus(null)
    }

    fun consultarPorStatus(status: String): List<CaixaDeAgua> {
        return listarComFiltroDeStatus(status)
    }

    private fun listarComFiltroDeStatus(status: String?): List<CaixaDeAgua> {
        val produtos = mutableListOf<CaixaDeAgua>()
        try {
            c = JPAConexao().conectar()

            var sql = "SELECT * FROM caixa_de_agua"
            if (status != null) {
                sql += " WHERE status = ?"
            }

            val stmt = c!!.prepareStatement(sql)
            if (status != null) {
                stmt.setString(1, status)
            }

            val resultado = stmt.executeQuery()

            while (resultado.next()) {
                val dimensaoArray = resultado.getArray("dimensao").array as Array<*>
                val dimensao = dimensaoArray.map { (it as Number).toDouble() }.toMutableList()

                val produto = CaixaDeAgua(
                    id = resultado.getLong("id"),
                    marca = resultado.getString("marca"),
                    modelo = resultado.getString("modelo"),
                    dimensao = dimensao,
                    cor = Cor.valueOf(resultado.getString("cor")),
                    material = Material.valueOf(resultado.getString("material")),
                    formato = Formatos.valueOf(resultado.getString("formato")),
                    fornecedor = resultado.getString("fornecedor"),
                    preco = resultado.getBigDecimal("preco"),
                    dtCriacao = resultado.getTimestamp("dt_criacao").toLocalDateTime(),
                    quantidade = resultado.getInt("quantidade"),
                    status = resultado.getString("status"),
                )
                produtos.add(produto)
            }

            stmt.close()
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
        return produtos
    }

    fun editar(caixa: CaixaDeAgua, id: Long): CaixaDeAgua {
        try {
            c = JPAConexao().conectar()

            val sql = "UPDATE caixa_de_agua SET marca = ?, modelo = ?, dimensao = ?, cor = ?, " +
                    "material = ?, formato = ?, fornecedor = ?, preco = ?, quantidade = 0 WHERE id = ?"

            val stmt = c!!.prepareStatement(sql)
            val doublePrecision = c!!.createArrayOf("float8", caixa.dimensao.toTypedArray())

            stmt.setString(1, caixa.marca)
            stmt.setString(2, caixa.modelo)
            stmt.setArray(3, doublePrecision)
            stmt.setString(4, caixa.cor.name)
            stmt.setString(5, caixa.material.name)
            stmt.setString(6, caixa.formato.name)
            stmt.setString(7, caixa.fornecedor)
            stmt.setBigDecimal(8, caixa.preco)
            stmt.setLong(9, id)

            stmt.executeUpdate()
            stmt.close()

            caixa.id = id
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
        return caixa
    }

    fun alterarCampo(id: Long, campo: String, valor: String) {
        try {
            c = JPAConexao().conectar()
            val sql = "UPDATE caixa_de_agua SET $campo = ? WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setString(1, valor)
            stmt.setLong(2, id)
            stmt.executeUpdate()
            stmt.close()
        } catch (e: SQLException) {
            println(e.printStackTrace())
        } finally {
            c?.close()
        }
    }
}
