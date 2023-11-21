package com.example.flowdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 * Criando stateFlow com kotlin, o flow trasnmite dados baseados no tipo informado emitindo atualizacoes
 * de estado atuais ou novas para seus coletores.
 */

class DemoViewModel: ViewModel() {

    // criando fluxo stateFlow com MutableStateFlow() que recebera um valor inicial
    private val _stateFlow = MutableStateFlow(0)
    // criando variavel de referencia ao state flow
    val stateFlow = _stateFlow.asStateFlow()

    // funcao para incrementar valor ao mutbleState
    fun increaseValue() {
        _stateFlow.value += 1
    }

    // criando sharedFlow que fara tudo que o MutableStateFlow faz com algumas diferencas
    // o MutableSharedFlow defini o limite vezes em que o fluxo sera reproduzido
    // e um valor no buffer que definira como ele deve funcionar
    // EX: first in first out -> o primeiro valor a entrar no buffer vai ser o primeiro a sair quando ele tiver cheio
    // EX: suspend buffer -> quando o buffer estiver cheio ele sera suspenso
    private val _sharedFlow = MutableSharedFlow<Int>(
        replay = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()

    // criando funcao que iniciara uam isntancia do sharedFlow chamando o emit()
    fun startSharedFlow() {
        viewModelScope.launch {
            for (i in 1..5) {
                _sharedFlow.emit(i)
                delay(2000)
            }
        }
    }

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

    fun doublet(value: Int) = flow {
        emit(value)
        delay(1000)
        emit(value + value)
    }
}