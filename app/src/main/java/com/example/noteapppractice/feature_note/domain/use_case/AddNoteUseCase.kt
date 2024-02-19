package com.example.noteapppractice.feature_note.domain.use_case

import com.example.noteapppractice.feature_note.domain.model.InvalidNoteException
import com.example.noteapppractice.feature_note.domain.model.Note
import com.example.noteapppractice.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNoteUseCase(
    private val noteRepository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("the title of the note cant be empty!!")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("the content of the note cant be empty!!")
        }
        noteRepository.upsertNote(note)
    }
}