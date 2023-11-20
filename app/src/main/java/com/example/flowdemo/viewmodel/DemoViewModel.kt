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

    // criando modificador de tipo stateFlow retornando o novo valor
    // usaremos a collection map que itera sobre o valor de 0 a 9 e formamos uma string com esses valores
    // e a collection filter para filtrar valores pares
    /*val newFlow = myFlow.filter {
        it % 2 == 0
    }.map {
        "Current value = $it"
    }*/

    val newFlow = myFlow.map {
        "Current value = $it"
    }

    // criando modificador de tipo stateFlow retornando o novo valor
    // usando o transform que é semelhante ao map com a vantagem de podermos adcionar multiplos valores
    val flowTransform = myFlow.transform {
        emit("Value = $it")
        delay(1000)
        val doubled = it * 2
        emit("Multiple value = $doubled")
    }
}