package com.example.todolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                // Estado para controlar si se muestra la splash screen o el contenido principal
                var showSplashScreen by remember { mutableStateOf(true) }

                // Efecto para cambiar el estado después de un tiempo
                LaunchedEffect(Unit) {
                    delay(3000) // Muestra la splash screen por 3 segundos (3000 milisegundos)
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    SplashScreen()
                } else {
                    MainContentScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(), // Ocupa_todo el espacio disponible
        verticalArrangement = Arrangement.Center, // Centra verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
    ) {
        Text(
            text = "Todo List",
            fontSize = 50.sp,
            color = Color(red = 0, green = 120, blue = 212)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentScreen() {
    val context = LocalContext.current // Get the current context for Toast

    Scaffold(
        topBar = { MyHeader() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp), // Add some padding around the content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically too
        ) {
            Text(
                text ="Bienvenido a DragonStats, la app en donde podrás ver los resultados de la DragonsLeague, presiona el botón para continuar.",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { Toast.makeText(context, "No existen resultados de la Liga T-T", Toast.LENGTH_LONG).show() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(red = 13, green = 113, blue = 50), // Your custom background color
                    contentColor = Color.White
                )
            ) {
                Text("Ver resultados de la Liga")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHeader(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = "ToDoList",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(red = 0, green = 120, blue = 212)
        ),
        modifier = modifier
    )
}
