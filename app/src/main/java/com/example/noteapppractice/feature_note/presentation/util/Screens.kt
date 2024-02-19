package com.example.noteapppractice.feature_note.presentation.util

sealed class Screens(val route:String){
    object NotesScreen:Screens(route = "note_screen")
    object AddEditNoteScreen:Screens(route = "add_edit_note_screen")
}
