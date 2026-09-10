package com.example.huilsonbackendprojeto1b.service

import com.example.huilsonbackendprojeto1b.repositorio.JPACaixa
import java.math.BigDecimal

class CaixaService {
    fun consultarSaldo(): BigDecimal? {
        return JPACaixa.consultarSaldo()
    }
}
