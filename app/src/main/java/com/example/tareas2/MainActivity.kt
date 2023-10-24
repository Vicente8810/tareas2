package com.example.tareas2



import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tareas2.ui.theme.Tareas2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tareas2Theme {
                TareasApp()
            }
        }
    }
}

data class Tarea(val id: Int, val descripcion: String, var completada: Boolean = false)

val listaTareas = mutableStateListOf<Tarea>() // Lista vacía al inicio

@Composable
fun ListaDeTareas(
    tareas: List<Tarea>,
    onTareaCompletada: (Tarea) -> Unit,
    onTareaBorrada: (Tarea) -> Unit
) {
    LazyColumn {
        items(tareas) { tarea ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { isChecked ->
                        tarea.completada = isChecked
                        onTareaCompletada(tarea)
                    }
                )
                Text(
                    text = tarea.descripcion,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    style = TextStyle(fontSize = 16.sp)
                )
                IconButton(
                    onClick = { onTareaBorrada(tarea) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Borrar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TareasApp() {
    val tareas by remember { mutableStateOf(listaTareas) }
    var nuevaTarea by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Lista de Tareas") }
            )
        },
        content = {
            Column {
                TextField(
                    value = nuevaTarea,
                    onValueChange = { nuevaTarea = it },
                    label = { Text("Nueva Tarea") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Button(
                    onClick = {
                        if (nuevaTarea.isNotBlank()) {
                            val newId = tareas.size
                            tareas.add(Tarea(newId, nuevaTarea))
                            nuevaTarea = ""
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp)
                ) {
                    Text("Agregar Tarea")
                }
                ListaDeTareas(
                    tareas = tareas,
                    onTareaCompletada = { tarea ->
                        tarea.completada = !tarea.completada
                    },
                    onTareaBorrada = { tarea ->
                        tareas.remove(tarea)
                    }
                )
            }
        }
    )
}