package com.example.note.feature_note.presentation.add_edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.feature_note.domain.model.InvalidNoteException
import com.example.note.feature_note.domain.model.Note
import com.example.note.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    var state by mutableStateOf(AddEditNoteState())
        private set

    var effect = MutableSharedFlow<AddEditNoteEffect>()
        private set

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.GetNote -> getNote(event.id)
            is AddEditNoteEvent.EnteredTitle -> enteredTitle(event.value)
            is AddEditNoteEvent.ChangeTitleFocus -> changeTitleFocus(event.focusState)
            is AddEditNoteEvent.EnteredContent -> enteredContent(event.value)
            is AddEditNoteEvent.ChangeContentFocus -> changeContentFocus(event.focusState)
            is AddEditNoteEvent.ChangeColor -> changeColor(event.color)
            is AddEditNoteEvent.DeleteNote -> deleteNote()
            is AddEditNoteEvent.SaveNote -> saveNote()
        }
    }

    private fun getNote(id: Int) {
        if (id != -1) {
            viewModelScope.launch {
                noteUseCases.getNote(id)?.also { note ->
                    state = state.copy(
                        noteId = id,
                        title = state.title.copy(
                            text = note.title,
                            isHintVisible = false
                        ),
                        content = state.content.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                    )
                }
            }
        }
    }

    private fun enteredTitle(title: String) {
        state = state.copy(title = state.title.copy(text = title))
    }

    private fun changeTitleFocus(focusState: FocusState) {
        state = state.copy(
            title = state.title.copy(
                isHintVisible = !focusState.isFocused &&
                        state.title.text.isBlank()
            )
        )
    }

    private fun enteredContent(text: String) {
        state = state.copy(content = state.content.copy(text = text))
    }

    private fun changeContentFocus(focusState: FocusState) {
        state = state.copy(
            content = state.content.copy(
                isHintVisible = !focusState.isFocused &&
                        state.content.text.isBlank()
            )
        )
    }

    private fun changeColor(color: Int) {
        state = state.copy(color = color)
    }

    private fun saveNote() {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(
                    Note(
                        title = state.title.text,
                        content = state.content.text,
                        id = state.noteId,
                        color = state.color,
                        timestamp = System.currentTimeMillis()
                    )
                )
                effect.emit(AddEditNoteEffect.SaveNote)
            } catch (e: InvalidNoteException) {
                effect.emit(
                    AddEditNoteEffect.ShowSnackBar(
                        message = e.message ?: "Couldn't save note"
                    )
                )
            }
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            noteUseCases.deleteNote(
                Note(
                    title = state.title.text,
                    content = state.content.text,
                    id = state.noteId,
                    color = state.color,
                    timestamp = System.currentTimeMillis()
                )
            )
            effect.emit(AddEditNoteEffect.SaveNote)
        }
    }

}