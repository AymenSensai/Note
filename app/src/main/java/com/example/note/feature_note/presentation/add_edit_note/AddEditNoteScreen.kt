package com.example.note.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.note.R
import com.example.note.feature_note.domain.model.Note
import com.example.note.feature_note.presentation.add_edit_note.components.ColorOption
import com.example.note.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.example.note.ui.theme.Blue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteRoute(
    id: Int,
    noteColor: Int,
    state: AddEditNoteState,
    effect: MutableSharedFlow<AddEditNoteEffect>,
    onEvent: (AddEditNoteEvent) -> Unit,
    onNavigateToNotesScreen: () -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        onEvent.invoke(AddEditNoteEvent.GetNote(id))
    }

    LaunchedEffect(key1 = true) {
        effect.collectLatest { effect ->
            when (effect) {
                is AddEditNoteEffect.ShowSnackBar -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(effect.message)
                    }
                }

                is AddEditNoteEffect.SaveNote -> onNavigateToNotesScreen()
            }
        }
    }

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else state.color)
        )
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState, snackbar = {
                Snackbar(
                    snackbarData = it,
                    containerColor = Blue,
                    contentColor = White
                )
            })
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Note.noteColors.forEach { color ->
                        val colorInt = color.toArgb()
                        ColorOption(
                            color, onClick = {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AddEditNoteEvent.SaveNote)
                },
                modifier = Modifier.offset(y = 30.dp),
                containerColor = Color(0xFF2BC2EC),
                contentColor = White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save note",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = noteBackgroundAnimatable.value
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateToNotesScreen) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Black
                    )
                }
                if (id != -1) {
                    IconButton(onClick = { onEvent(AddEditNoteEvent.DeleteNote) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash_icon),
                            contentDescription = "Delete Note",
                            tint = Black
                        )
                    }
                }
            }

            TransparentHintTextField(
                text = state.title.text,
                hint = state.title.hint,
                onValueChange = { text ->
                    onEvent(AddEditNoteEvent.EnteredTitle(text))
                },
                onFocusChange = { focus ->
                    onEvent(AddEditNoteEvent.ChangeTitleFocus(focus))
                },
                isHintVisible = state.title.isHintVisible,
                textStyle = MaterialTheme.typography.headlineSmall,
                singleLine = true,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = state.content.text,
                hint = state.content.hint,
                onValueChange = { text ->
                    onEvent(AddEditNoteEvent.EnteredContent(text))
                },
                onFocusChange = { focus ->
                    onEvent(AddEditNoteEvent.ChangeContentFocus(focus))
                },
                isHintVisible = state.content.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}