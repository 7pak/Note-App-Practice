package com.example.noteapppractice.feature_note.presentation.notes

import com.example.noteapppractice.feature_note.domain.model.Note
import com.example.noteapppractice.feature_note.domain.util.NoteOrder

sealed class NotesEvent{
    data class Order(val netOrder: NoteOrder):NotesEvent()
    data class DeleteNote(val note: Note):NotesEvent()

    object RestoreNote:NotesEvent()
    object ToggleOrderSection:NotesEvent()
}
