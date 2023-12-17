package com.example.note.feature_note.presentation.notes

sealed class NotesEvent {
    data object GetNotes : NotesEvent()
    data class SearchNotes(val query: String) : NotesEvent()
}