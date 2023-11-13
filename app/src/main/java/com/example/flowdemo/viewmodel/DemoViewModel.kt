package com.example.flowdemo.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Criando stateFlow com kotlin, o flow trasnmite dados baseados no tipo informado emitindo atualizacoes
 * de estado atuais ou novas para seus coletores.
 */

class DemoViewModel: ViewModel() {

    // criando stateFlow do tipo int que fara uma contagem de 0 a 9 com delay de 2 segundos
    val myFlow: Flow<Int> = flow {
        for (i in 0..9) {
            emit(i)
            delay(2000)
        }
    }
}