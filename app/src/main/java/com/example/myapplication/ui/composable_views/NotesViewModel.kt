package com.example.myapplication.ui.composable_views

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.NoteModel

class NotesViewModel : ViewModel() {
    private val _notes = mutableStateListOf<NoteModel>()
    val notes: List<NoteModel> = _notes

    fun addNote(title: String, description: String) {
        _notes.add(NoteModel(title = title, description = description))
    }

    fun updateNote(id: String, title: String, description: String) {
        val note = _notes.find { it.id.toString() == id }
        note?.let {
            it.title = title
            it.description = description
        }
    }

    fun deleteNote(note: NoteModel) {
        _notes.remove(note)
    }
}