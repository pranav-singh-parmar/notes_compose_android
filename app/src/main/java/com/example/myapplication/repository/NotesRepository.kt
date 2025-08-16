package com.example.myapplication.repository

import com.example.myapplication.dao.NotesDao
import com.example.myapplication.model.NoteModel
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDao: NotesDao) {

    val allNotes: Flow<List<NoteModel>> = notesDao.getAllTodos()

    suspend fun insert(note: NoteModel) = notesDao.insert(note)

    suspend fun update(note: NoteModel) = notesDao.update(note)

    suspend fun delete(note: NoteModel) = notesDao.delete(note)
}
