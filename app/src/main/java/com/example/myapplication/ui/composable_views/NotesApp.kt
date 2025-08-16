package com.example.myapplication.ui.composable_views

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.model.NoteModel

@Composable
fun NotesApp() {
    val navController = rememberNavController()
    val notes = remember { mutableStateListOf<NoteModel>() }

    NavHost(navController = navController, startDestination = "list") {
        // List screen
        composable("list",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) },
//            enterTransition = {
//                slideIntoContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) {
            NoteListScreen(
                notes = notes,
                onAddClick = { navController.navigate("add") },
                onItemClick = { noteID ->
                    navController.navigate("edit/$noteID")
                },
                onDeleteClick = { note ->
                    notes.remove(note)
                }
            )
        }

        composable("add",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) },
//            enterTransition = {
//                slideIntoContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
//                    animationSpec = tween(700)
//                )
//            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) {
            AddEditNoteScreen(
                initialNote = null,
                onSave = { name, model ->
                    notes.add(NoteModel(title = name, description = model))
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            "edit/{noteID}",
            arguments = listOf(navArgument("noteID") { type = NavType.StringType }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }) { backStackEntry ->
            val noteID = backStackEntry.arguments?.getString("noteID")
            val note = notes.find { it.id.toString() == noteID }

            if (note != null) {
                AddEditNoteScreen(
                    initialNote = note,
                    onSave = { name, model ->
                        note.title = name
                        note.description = model
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}