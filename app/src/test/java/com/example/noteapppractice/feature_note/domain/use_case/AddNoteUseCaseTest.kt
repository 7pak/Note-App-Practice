package com.example.noteapppractice.feature_note.domain.use_case

import com.example.noteapppractice.feature_note.data.repository.FakeNoteRepository
import com.example.noteapppractice.feature_note.domain.model.InvalidNoteException
import com.example.noteapppractice.feature_note.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class AddNoteUseCaseTest {

    lateinit var addNote:AddNoteUseCase
    lateinit var fakeNoteRepository: FakeNoteRepository
    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        addNote = AddNoteUseCase(fakeNoteRepository)

    }

    @Test(expected = InvalidNoteException::class)
    fun `Throw error when the title is not blank`() = runBlocking {
        val note = Note(title = "", content = "this is content",0, timestamp = System.currentTimeMillis())
       addNote(note)
    }

    @Test(expected = InvalidNoteException::class)
    fun `Throw error when the content is not blank`() = runBlocking {
        val note = Note(title = "title", content = "",0, timestamp = System.currentTimeMillis())
        addNote(note)
    }

    @Test
    fun `Note is added when title and content are valid`() = runBlocking {
        val note = Note(title = "Title", content = "Some content", color = 0, timestamp = System.currentTimeMillis(),id = 30)
        addNote(note)

        val insertedNote = fakeNoteRepository.getNoteById(note.id!!)
        assertThat(note).isEqualTo(insertedNote)
    }
}