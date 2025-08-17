package com.example.myapplication.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.NotesDatabase
import com.example.myapplication.repository.NotesRepository
import com.example.myapplication.ui.composable_views.NotesApp
import com.example.myapplication.ui.composable_views.NotesModelFactory
import com.example.myapplication.ui.composable_views.NotesViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = NotesDatabase.getDatabase(this)
        val repository = NotesRepository(database.todoDao())
        notesViewModel = ViewModelProvider(
            this,
            NotesModelFactory(repository)
        )[NotesViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NotesApp(notesViewModel = notesViewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesAppPreview() {
    MyApplicationTheme {
        NotesApp()
    }
}
