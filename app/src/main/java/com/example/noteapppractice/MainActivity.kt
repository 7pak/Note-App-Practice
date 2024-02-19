package com.example.noteapppractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapppractice.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.noteapppractice.feature_note.presentation.notes.NotesScreen
import com.example.noteapppractice.feature_note.presentation.util.Screens
import com.example.noteapppractice.ui.theme.NoteAppPracticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppPracticeTheme {
                val navController  = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    NavHost(navController =navController , startDestination = Screens.NotesScreen.route){
                        composable(route = Screens.NotesScreen.route){
                            NotesScreen(navController = navController)
                        }
                        composable(route = Screens.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}", arguments = listOf(
                            navArgument(name = "noteId"){
                                type = NavType.IntType
                                defaultValue = -1
                            }, navArgument("noteColor"){
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )){entries->
                            val color = entries.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(navController = navController, noteColor =color )
                        }
                    }
                }
            }
        }
    }
}
