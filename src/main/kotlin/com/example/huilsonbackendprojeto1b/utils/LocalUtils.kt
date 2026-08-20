package com.example.huilsonbackendprojeto1b.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

fun formatacaoDinheiro(valor: BigDecimal): String {
    return valor.setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")
}

fun userInput(msg: String, aceitarBranco: Boolean = false): String{
    var read: String
    do{
        print(msg)
        read = readln().trim()
        if(!aceitarBranco && read.isBlank()){
            println("Campo não aceita valores em branco")
            continue
        }
        break
    } while(true)

    return read
}

/**
 * formatacao: String -> dd/mm/aaaa, aaaa/mm/dd, etc
 * */

fun validarCampoData(msg: String): LocalDate {
    var read: String = readln().trim()
    val regex: Regex = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$".toRegex()
    val data: List<String> = read.split("/")

    return LocalDate.of(data[0].toInt(), data[1].toInt(), data[2].toInt())
}

/**
 * tipo: String -> int, double
 * */
fun validarCampoNumerico(msg: String, aceitarDecimal: Boolean = true, retornarComPonto: Boolean = true, aceitarBranco: Boolean = false): String{
    val validarDouble = if (aceitarDecimal) "[(.,|)]?[0-9]+" else ""
    val regex = "^[0-9]*${validarDouble}$".toRegex()
    var read: String

    do{
        read = userInput(msg, aceitarBranco)

        if(aceitarBranco && read.isBlank()){
            return ""
        }

        if(!regex.matches(read)){
            print("Número inválido")
            if (!aceitarDecimal) println(", precisa ser inteiro") else println()
            continue
        }
        break
    } while (true)

    return if (retornarComPonto) read.replace(",", ".") else read
}
