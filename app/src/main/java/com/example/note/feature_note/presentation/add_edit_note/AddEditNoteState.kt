package com.example.note.feature_note.presentation.add_edit_note

import androidx.compose.ui.graphics.toArgb
import com.example.note.ui.theme.White

data class AddEditNoteState(
    val noteId: Int? = null,
    val title: NoteTextFieldState = NoteTextFieldState(hint = "Enter title..."),
    val content: NoteTextFieldState = NoteTextFieldState(hint = "Enter some content"),
    val color: Int = White.toArgb(),
)