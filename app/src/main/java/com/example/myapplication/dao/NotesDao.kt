package com.example.myapplication.dao

import androidx.room.*
import com.example.myapplication.model.NoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAllTodos(): Flow<List<NoteModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: NoteModel)

    @Update
    suspend fun update(todo: NoteModel)

    @Delete
    suspend fun delete(todo: NoteModel)
}
