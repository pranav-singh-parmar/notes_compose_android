package com.example.myapplication.ui.composable_views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.NoteModel
import com.example.myapplication.repository.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val notes = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(name: String, description: String) {
        viewModelScope.launch {
            repository.insert(NoteModel(title = name, description = description))
        }
    }

    fun update(noteModel: NoteModel) {
        viewModelScope.launch {
            repository.update(noteModel)
        }
    }

    fun delete(noteModel: NoteModel) {
        viewModelScope.launch {
            repository.delete(noteModel)
        }
    }
}

class NotesModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
