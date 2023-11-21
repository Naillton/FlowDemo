package com.example.flowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.flowdemo.ui.theme.FlowDemoTheme
import com.example.flowdemo.viewmodel.DemoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime. *
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.zip
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenSetup()
                }
            }
        }
    }
}

@Composable
fun ScreenSetup(model: DemoViewModel = viewModel()) {
    // MainScreen(model.myFlow)
    // MultipleFlows()
    // MutableState(model)
    MutableSharedStateFlow(model = model)
}

@OptIn(FlowPreview::class)
@Composable
fun MainScreen(flow: Flow<Int>) {
    // criando estado que coletara as informacoes vindas do stateFlow
    // val count by model.newFlow.collectAsState(initial = "Current value")

    // criando estado que coletara as informacoes do stateFlow usando escopo de corrotinas
    // var count by rememberSaveable { mutableStateOf(0) }
    // usando lancamento de efeitos que tera um try/finally para o decorrer e o fim do flow
    /*LaunchedEffect(Unit) {
        // adcionando buffer ao fluxo, medindo o tempo de processamento do fluxo
        val enlapsedTime = measureTimeMillis {
            try {
                flow.buffer().collect {
                    count = it
                    delay(1000)
                }
            } finally {
                count = "End of flow"
            }
        }
        count = "Duration = $enlapsedTime"
    }*/

    // usando o reduce para iterar sobre os valores do state
    // o reduce possui dois parametros o accumulator e o value
    // o accumulator ira acumular o valor e o value sera o proximo valor do state
    // podemos usar o fold(0) metodo que trabalha similarmente como o reduce, so precisamos passar
    // um valor inicial como parametro
    var count by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        flow.reduce { acc, value ->
            count = acc
            acc + value
        }
    }

    // criando flow flatering, concatenando fluxos diferentes em um unico fluxo
    /*var count by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        model.myFlow.flatMapConcat { model.doublet(it) }
            .collect{ count = it }
    }*/

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$count", style = TextStyle(fontSize = 40.sp))
    }

}

/**
 * Combinando multiplos fluxos em um unico fluxo usando zip() e combine()
 */
@Composable
fun MultipleFlows() {
    var count by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {

        val flow1 = (1..5).asFlow().onEach { delay(1000) }

        val flow2 = flowOf("one", "two", "three", "four", "five").onEach { delay(1500) }

        flow1.zip(flow2) { value, string -> "$value, $string"}
            .collect{ count = it }

    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = count, style = TextStyle(fontSize = 40.sp))
    }
}

@Composable
fun MutableState(model: DemoViewModel) {
    val count by model.stateFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { model.increaseValue() }
        ) {
            Text(text = "Increase Value")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Value = $count")
    }
}

@Composable
fun MutableSharedStateFlow(model: DemoViewModel) {
    val count by model.sharedFlow.collectAsState(0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { model.startSharedFlow() }
        ) {
            Text(text = "Increase Value")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Value = $count")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    FlowDemoTheme {
        ScreenSetup()
    }
}