package com.example.note.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.ui.theme.BlueGreen
import com.example.note.ui.theme.CarnationPink
import com.example.note.ui.theme.LightSalmonPink
import com.example.note.ui.theme.Orange
import com.example.note.ui.theme.SkyBlue
import com.example.note.ui.theme.White

@Entity
data class Note(
    val title: String,
    val content: String,
    val color: Int,
    val timestamp: Long,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(White, Orange, SkyBlue, CarnationPink, BlueGreen, LightSalmonPink)
    }
}

class InvalidNoteException(message: String): Exception(message)