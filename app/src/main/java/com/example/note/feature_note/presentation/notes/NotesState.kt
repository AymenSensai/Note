package com.example.note.feature_note.presentation.notes

import com.example.note.feature_note.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
    val searchedNotes: List<Note> = emptyList(),
    val query: String = ""
)