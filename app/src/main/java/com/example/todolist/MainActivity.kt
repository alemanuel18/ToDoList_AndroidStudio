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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.todolist.R
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.delay

// Data class para manejar las tareas
data class Task(val text: String, val isCompleted: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                var showSplashScreen by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(3000)
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
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Todo List",
            fontSize = 50.sp,
            color = Color(0xFF0D7132)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentScreen() {
    val context = LocalContext.current

    // Cambiado a lista de Task objects
    val taskList = remember { mutableStateListOf<Task>() }
    var newTask by remember { mutableStateOf("") }

    Scaffold(
        topBar = { MyHeader() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.messi),
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

                androidx.compose.material3.OutlinedTextField(
                    value = newTask,
                    onValueChange = { newTask = it },
                    label = { Text("Nueva tarea") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Button(
                    onClick = {
                        if (newTask.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "El campo está vacío. Escribe una tarea.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            taskList.add(Task(newTask.trim()))
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

                // Pasamos la lista y una función para toggle
                SimpleLazyColumn(
                    tasks = taskList,
                    onToggleTask = { index ->
                        val task = taskList[index]
                        taskList[index] = task.copy(isCompleted = !task.isCompleted)
                    }
                )
            }
        }
    }
}

@Composable
fun SimpleLazyColumn(
    tasks: List<Task>,
    onToggleTask: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        itemsIndexed(tasks) { index, task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        if (task.isCompleted) Color.Gray.copy(alpha = 0.2f)
                        else Color.Red.copy(alpha = 0.2f)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    text = if (task.isCompleted) "✅ ${task.text}" else task.text,
                    style = androidx.compose.ui.text.TextStyle(
                        color = if (task.isCompleted) Color.Gray else Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = { onToggleTask(index) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.isCompleted) Color.Red else Color(0xFF0D7132)
                    )
                ) {
                    Text(
                        text = if (task.isCompleted) "Desmarcar" else "Completar",
                        color = Color.White
                    )
                }
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
            containerColor = Color(0xFF0D7132)
        ),
        modifier = modifier
    )
}
