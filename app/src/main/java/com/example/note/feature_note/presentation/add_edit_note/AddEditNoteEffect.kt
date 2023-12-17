package com.example.note.feature_note.presentation.add_edit_note

sealed class AddEditNoteEffect {
    data class ShowSnackBar(val message: String) : AddEditNoteEffect()
    data object SaveNote : AddEditNoteEffect()
}