package com.example.note.feature_note.domain.use_case

import com.example.note.feature_note.domain.model.Note
import com.example.note.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(query: String): Flow<List<Note>> {
        return repository.searchNotes(query)
    }

}