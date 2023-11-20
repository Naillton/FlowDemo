package com.example.flowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
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
    MainScreen(model.newFlow)
}

@Composable
fun MainScreen(flow: Flow<String>) {
    // criando estado que coletara as informacoes vindas do stateFlow
    // val count by model.newFlow.collectAsState(initial = "Current value")

    // criando estado que coletara as informacoes do stateFlow usando escopo de corrotinas
    var count by rememberSaveable { mutableStateOf("Current value =") }
    // usando lancamento de efeitos que tera um try/finally para o decorrer e o fim do flow
    LaunchedEffect(Unit) {
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
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = count, style = TextStyle(fontSize = 40.sp))
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    FlowDemoTheme {
        ScreenSetup()
    }
}