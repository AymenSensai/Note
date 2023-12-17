package com.example.note.feature_note.presentation.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.note.feature_note.domain.model.Note
import com.example.note.feature_note.presentation.notes.components.NoteItem
import com.example.note.feature_note.presentation.notes.components.SearchField
import com.example.note.ui.theme.Blue
import com.example.note.ui.theme.White

@Composable
fun NotesScreen(
    state: NotesState,
    onEvent: (NotesEvent) -> Unit,
    onAddNote: () -> Unit,
    onNavigateToNote: (Note) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        onEvent(NotesEvent.GetNotes)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddNote()
                },
                containerColor = Blue,
                contentColor = White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        containerColor = Color.White,
        floatingActionButtonPosition = FabPosition.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            SearchField(
                text = state.query,
                onValueChange = { query -> onEvent(NotesEvent.SearchNotes(query)) })

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 16.dp
            ) {

                items(if (state.query.isEmpty()) state.notes else state.searchedNotes) { note ->
                    NoteItem(
                        note = note,
                        onNavigateToAddEditNoteScreen = { onNavigateToNote(note) },
                    )
                }
            }
        }
    }

}