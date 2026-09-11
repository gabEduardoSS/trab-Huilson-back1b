package com.example.huilsonbackendprojeto1b.repositorio

import com.example.huilsonbackendprojeto1b.enumeradores.Cor
import com.example.huilsonbackendprojeto1b.enumeradores.Formato
import com.example.huilsonbackendprojeto1b.enumeradores.Material
import com.example.huilsonbackendprojeto1b.enumeradores.TipoMovimentacao
import com.example.huilsonbackendprojeto1b.enumeradores.TipoPessoa
import com.example.huilsonbackendprojeto1b.enumeradores.TipoTransacao
import com.example.huilsonbackendprojeto1b.financeiro.Transacao
import com.example.huilsonbackendprojeto1b.logistica.Movimentacao
import com.example.huilsonbackendprojeto1b.pessoas.Pessoa
import com.example.huilsonbackendprojeto1b.produto.CaixaDeAgua
import java.sql.Connection
import java.sql.SQLException

object JPATransacao {
    fun criarTransacao(transacao: Transacao, con: Connection): Transacao {
        try {
            val sqlInsert = "INSERT INTO transacao(valor, id_caixa, id_pessoa, tipo, status) VALUES(?, 1, ?, ?, 'PENDENTE') RETURNING id, status, data, saldo_anterior, saldo_posterior"
            val stmtInsert = con.prepareStatement(sqlInsert)
            stmtInsert.setBigDecimal(1, transacao.valor)
            stmtInsert.setLong(2, transacao.pessoa.id!!)
            stmtInsert.setString(3, transacao.tipoTransacao.name)

            val rs = stmtInsert.executeQuery()
            rs.next()

            transacao.id = rs.getLong("id")
            transacao.saldoAnterior = rs.getBigDecimal("saldo_anterior")
            transacao.saldoPosterior = rs.getBigDecimal("saldo_posterior")
            transacao.status = rs.getString("status")
            transacao.dataMovimentacao = rs.getTimestamp("data").toLocalDateTime()
            stmtInsert.close()
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}, ${e.message}")
        }
        return transacao
    }

    fun consultarTransacao(con: Connection? = null, tipo: TipoTransacao? = null): List<Transacao>{
        val transacoes = mutableListOf<Transacao>()
        var conexaoInterna: Connection? = null

        try{
            conexaoInterna = con ?: JPAConexao.conectar()

            var campos = "t.id AS id_transacao, t.id_pessoa, t.valor, t.tipo AS tipo_transacao, t.status, t.saldo_anterior, t.saldo_posterior, t.data, " +
                    "p.nome, p.cpf, p.email, p.telefone, p.cidade, p.endereco, p.dt_nasc, p.tipo AS tipo_pessoa"
            var sql = "SELECT $campos FROM transacao t join pessoa p on t.id_pessoa = p.id"
            if(tipo != null) sql += " WHERE t.tipo = ?"

            val stmt = conexaoInterna!!.prepareStatement(sql)
            if(tipo != null) stmt.setString(1, tipo.name)
            val rs = stmt.executeQuery()

            while (rs.next()) {
                val pessoa = Pessoa(
                    id = rs.getLong("id_pessoa"),
                    nome = rs.getString("nome"),
                    cpf = rs.getString("cpf"),
                    email = rs.getString("email"),
                    telefone = rs.getString("telefone"),
                    cidade = rs.getString("cidade"),
                    endereco = rs.getString("endereco"),
                    dtNasc = rs.getDate("dt_nasc").toLocalDate(),
                    tipo = TipoPessoa.valueOf(rs.getString("tipo_pessoa")),
                )

                val transacao = Transacao(
                    id = rs.getLong("id_transacao"),
                    valor = rs.getBigDecimal("valor"),
                    pessoa = pessoa,
                    tipoTransacao = TipoTransacao.valueOf(rs.getString("tipo_transacao"))
                )
                transacao.saldoAnterior = rs.getBigDecimal("saldo_anterior")
                transacao.saldoPosterior = rs.getBigDecimal("saldo_posterior")
                transacao.status = rs.getString("status")
                transacao.dataMovimentacao = rs.getTimestamp("data").toLocalDateTime()

                transacoes.add(transacao)
            }
            stmt.close()
        } catch (e: SQLException) {
            println("ERRO: ${e.stackTrace.joinToString(", ")}, ${e.message}")
        } finally {
            if(con == null){
                conexaoInterna?.close()
            }
        }
        return transacoes
    }
}
