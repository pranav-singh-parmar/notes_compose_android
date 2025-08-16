package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.UUID

// ----------------------------
// Todo Model
// ----------------------------
data class NoteModel(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TodoApp()
            }
        }
    }
}

// ----------------------------
// Main App with Navigation
// ----------------------------
@Composable
fun TodoApp() {
    val navController = rememberNavController()
    val todos = remember { mutableStateListOf<NoteModel>() }

    NavHost(navController = navController, startDestination = "list") {
        // List screen
        composable("list",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) },
//            enterTransition = {
//                slideIntoContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) {
            TodoListScreen(
                todos = todos,
                onAddClick = { navController.navigate("add") },
                onItemClick = { todoId ->
                    navController.navigate("edit/$todoId")
                },
                onDeleteClick = { todo ->
                    todos.remove(todo)
                }
            )
        }
        // Add new todo
        composable("add",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) },
//            enterTransition = {
//                slideIntoContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) {
            AddTodoScreen(
                initialTodo = null,
                onSave = { name, model ->
                    todos.add(NoteModel(title = name, description = model))
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        // Edit existing todo
        composable(
            "edit/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.StringType }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")
            val todo = todos.find { it.id.toString() == todoId }

            if (todo != null) {
                AddTodoScreen(
                    initialTodo = todo,
                    onSave = { name, model ->
                        todo.title = name
                        todo.description = model
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}

// ----------------------------
// Todo List Screen
// ----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    todos: List<NoteModel>,
    onAddClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onDeleteClick: (NoteModel) -> Unit,
    modifier: Modifier = Modifier
) {
    // Track which todo is selected for deletion
    var todoToDelete by remember { mutableStateOf<NoteModel?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Todo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // <-- AppBar BG color
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { innerPadding ->
        if (todos.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No todos available")
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                items(todos) { todo ->
                    TodoItem(
                        todo = todo,
                        onClick = { onItemClick(todo.id.toString()) },
                        onDeleteClick = { todoToDelete = todo }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    // Show confirmation dialog
    if (todoToDelete != null) {
        AlertDialog(
            onDismissRequest = { todoToDelete = null },
            title = { Text("Delete Todo") },
            text = { Text("Are you sure you want to delete \"${todoToDelete?.title}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    todoToDelete?.let { onDeleteClick(it) }
                    todoToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { todoToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ----------------------------
// Todo Item with Delete Button
// ----------------------------
@Composable
fun TodoItem(
    todo: NoteModel,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Title: ${todo.title}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Description: ${todo.description}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

// ----------------------------
// Add / Edit Todo Screen
// ----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    initialTodo: NoteModel?,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(initialTodo?.title ?: "") }
    var description by remember { mutableStateOf(initialTodo?.description ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (initialTodo == null) "Save" else "Update") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // <-- AppBar BG color
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onSave(title, description) }, enabled = title.isNotBlank()) {
                    Text(if (initialTodo == null) "Save" else "Update")
                }
                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        }
    }
}

// ----------------------------
// Preview
// ----------------------------
@Preview(showBackground = true)
@Composable
fun TodoAppPreview() {
    MyApplicationTheme {
        TodoApp()
    }
}
