package com.example.huilsonbackendprojeto1b.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

fun formatacaoDinheiro(valor: BigDecimal): String {
    return "R$" + valor.setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")
}


/**
 * Tipo: Int:
 *  1 -> Padrão;
 *  2 -> Email;
 *  3 -> Endereço(cidade, estado(sigla));
 *  4 - > Endereço(rua, número);
 * */
fun validarCampoString(msg: String, aceitarBranco: Boolean = false, tipo: Int = 1): String{
    val validar = if(tipo == 2) """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z.]+$""" else if(tipo == 3) """[A-Za-zÀ-ÿ\s]+,\s*[A-Z]{2}""" else if(tipo == 4) """.+,\s*[A-Za-z0-9]+""" else ""
    val regex = "^$validar$".toRegex()
    var read: String
    do{
        print(msg)
        read = readln().trim()

        if(aceitarBranco && read.isBlank()){
            return ""
        }

        if(tipo > 1 && !regex.matches(read)){
            when(tipo){
                2 -> println("Estrutura de email inválida")
                3, 4 -> println("Estrutura de endereço inválida")
            }
            continue
        }
        break
    } while(true)

    return read
}

fun validarCampoData(msg: String, aceitarBranco: Boolean = false): LocalDate? {
    val regex: Regex = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$".toRegex()
    var data: List<String>
    do{
        val read: String = validarCampoString(msg, aceitarBranco).trim()

        if(aceitarBranco && read.isBlank()){
            return null
        }

        if(!regex.matches(read)){
            println("Data inválida")
            continue
        }

        data = read.split("/")
        break
    } while(true)

    return LocalDate.of(data[2].toInt(), data[1].toInt(), data[0].toInt())
}


/**
 * Tipo: Int:
 *  1 -> Inteiro;
 *  2 -> Decimal;
 *  3 -> CPF ou Telefone(sem DDI);
 * */
fun validarCampoNumerico(msg: String, tipo: Int = 2, retornarComPonto: Boolean = true, aceitarBranco: Boolean = false, nonIntProof: String = ""): String{
    val validar = if(tipo == 1) """[0-9]*""" else if (tipo == 2) """[0-9]*[(\.,|)]?[0-9]+""" else if(tipo == 3) """[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}-?[0-9]{2}""" else ""
    val regex = "^$validar$".toRegex()
    var read: String

    do{
        read = validarCampoString(msg, aceitarBranco).replace(" ", "")

        if(aceitarBranco && read.isBlank()){
            return nonIntProof
        }

        if(!regex.matches(read)){
            print("Número inválido")
            when (tipo) {
                1 -> println(", precisa ser inteiro")
                else -> println()
            }
            continue
        }
        break
    } while (true)

    var retornoVal: String = ""
    when (tipo) {
        3 -> {
            retornoVal = read.replace(".", "").replace("-", "")
        }
        2 -> {
            retornoVal = if (retornarComPonto) read.replace(",", ".") else read
        }
        else -> {
            retornoVal = read
        }
    }

    return retornoVal
}

fun main(){
    val email = validarCampoString("Digite o email: ", tipo = 2, aceitarBranco = true)
    println(email)
}