package com.example.note.feature_note.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    var state by mutableStateOf(NotesState())
        private set

    private var searchJob: Job? = null

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.GetNotes -> getNotes()
            is NotesEvent.SearchNotes -> searchNotes(event.query)
        }
    }

    private fun getNotes() {
        noteUseCases.getNotes().onEach { notes ->
            state = state.copy(notes = notes)
        }.launchIn(viewModelScope)
    }

    private fun searchNotes(query: String) {
        state = state.copy(query = query)
        searchJob?.cancel()
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(500L)
                noteUseCases.searchNotes(query = query).collect { searchedNotes ->
                    state = state.copy(searchedNotes = searchedNotes)
                }
            }
        } else {
            state = state.copy(searchedNotes = emptyList())
        }
    }

}