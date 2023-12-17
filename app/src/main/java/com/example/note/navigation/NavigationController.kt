package com.example.note.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.note.feature_note.presentation.add_edit_note.AddEditNoteRoute
import com.example.note.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.example.note.feature_note.presentation.notes.NotesScreen
import com.example.note.feature_note.presentation.notes.NotesViewModel

@Composable
fun NavigationController() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NotesScreen.route
    ) {

        composable(Screen.NotesScreen.route) {
            val viewModel: NotesViewModel = hiltViewModel()
            val state = viewModel.state
            NotesScreen(
                state = state,
                onEvent = { viewModel.onEvent(it) },
                onAddNote = {
                    navController.navigate(Screen.AddEditNoteScreen.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToNote = { note ->
                    navController.navigate(
                        Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(
                    name = "noteId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "noteColor"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            }
        ) { entry ->
            val viewModel: AddEditNoteViewModel = hiltViewModel()
            val state = viewModel.state
            AddEditNoteRoute(
                id = entry.arguments?.getInt("noteId")!!,
                noteColor = entry.arguments?.getInt("noteColor")!!,
                state = state,
                onEvent = { viewModel.onEvent(it) },
                effect = viewModel.effect,
                onNavigateToNotesScreen = {
                    navController.popBackStack()
                },
            )
        }

    }

}