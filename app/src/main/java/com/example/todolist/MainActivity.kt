package com.example.todolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    // Estado de la lista y del texto de nueva tarea
    val lista = remember { mutableStateListOf<String>() }
    var newTask by remember { mutableStateOf("") }

    Scaffold(
        topBar = { MyHeader() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.mesi),
                contentDescription = "Imagen de fondo", 
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Campo de texto con label
                androidx.compose.material3.OutlinedTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = { Text("Nueva tarea") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                // Botón para agregar tarea
                Button(
                    onClick = {
                        if (newTask.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "El campo está vacío. Escribe una tarea.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            lista.add(newTask.trim())
                            newTask = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D7132),
                        contentColor = Color.White
                    )
                ) {
                    Text("Agregar tarea")
                }

                // Lista de tareas
                SimpleLazyColumn(lista)
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

@Composable
fun SimpleLazyColumn(items:List<String>) {
    var items by remember { mutableStateOf(items.map { it to false }) }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(items) { (text, isCompleted) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        if (isCompleted) Color.Gray.copy(alpha = 0.2f)
                        else Color.Red.copy(alpha=0.2f)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    text = if (isCompleted) "✅ $text" else text,
                    // Aquí se aplican los estilos de texto
                    style = androidx.compose.ui.text.TextStyle(
                        color = if (isCompleted) Color.Gray else Color.White,
                        fontSize = 18.sp // Define el tamaño del texto
                    ),
                    modifier = Modifier.weight(1f).padding(end = 8.dp) // Permite que el texto ocupe el espacio y añade padding al final
                )
                Button(onClick = {
                    items = items.map {
                        if (it.first == text) it.copy(second = !it.second) else
                            it.copy(second = false)
                    }
                }) {
                    Text(if (isCompleted) "Desmarcar" else "Completar")
                }
            }
        }
    }
}
