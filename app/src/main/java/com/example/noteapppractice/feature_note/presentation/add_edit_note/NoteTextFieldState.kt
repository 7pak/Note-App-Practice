package com.example.noteapppractice.feature_note.presentation.add_edit_note

data class NoteTextFieldState(
    var text:String = "",
    val hint:String = "",
    val isHintVisible:Boolean = true
)
