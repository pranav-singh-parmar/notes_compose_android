package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notes")
data class NoteModel(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String
)
