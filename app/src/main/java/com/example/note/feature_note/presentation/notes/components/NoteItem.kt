package com.example.note.feature_note.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.note.feature_note.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    onNavigateToAddEditNoteScreen: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color(note.color), shape = RoundedCornerShape(8.dp))
            .border(1.dp, color = Color.Gray.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
            .clickable { onNavigateToAddEditNoteScreen() }
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = note.content,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black,
        )
    }

}