package com.example.myapplication.model

import java.util.UUID

data class NoteModel(
    val id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String
)