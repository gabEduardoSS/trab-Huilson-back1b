package com.example.huilsonbackendprojeto1b.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun formatacaoDinheiro(valor: BigDecimal): String {
    return valor.setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")
}


/**
 * tipo: String -> int, double
 * */
fun validarCampoNumerico(mensagem: String, aceitarDecimal: Boolean = true, retornarComPonto: Boolean = true, aceitarBranco: Boolean = false): String{
    val validarDouble = if (aceitarDecimal) "[(.,|)]?[0-9]+" else ""
    val regex = "^[0-9]*${validarDouble}$".toRegex()
    var read: String

    do{
        print(mensagem)
        read = readln()

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
