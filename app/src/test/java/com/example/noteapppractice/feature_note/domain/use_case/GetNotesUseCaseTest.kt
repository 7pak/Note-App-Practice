package com.example.noteapppractice.feature_note.domain.use_case

import com.example.noteapppractice.feature_note.data.repository.FakeNoteRepository
import com.example.noteapppractice.feature_note.domain.model.Note
import com.example.noteapppractice.feature_note.domain.util.NoteOrder
import com.example.noteapppractice.feature_note.domain.util.OrderType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {

    lateinit var getNotes: GetNotesUseCase
    lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getNotes = GetNotesUseCase(fakeNoteRepository)

        val noteToInsert = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            noteToInsert.add(
                Note(
                    title = c.toString(),
                    content = c.toString(),
                    color = index,
                    timestamp = index.toLong()
                )
            )
        }
        noteToInsert.shuffle()
        runBlocking {
            noteToInsert.forEach {
                fakeNoteRepository.upsertNote(it)
            }
        }
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()
        for (i in 0 until notes.size - 1){
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }
    @Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Descending)).first()
        for (i in 0 until notes.size - 1){
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Ascending)).first()
        for (i in 0 until notes.size - 1){
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }
    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Descending)).first()
        for (i in 0 until notes.size - 1){
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by color ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Ascending)).first()
        for (i in 0 until notes.size - 1){
            assertThat(notes[i].color).isLessThan(notes[i+1].color)
        }
    }
    @Test
    fun `Order notes by color descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Descending)).first()
        for (i in 0 until notes.size - 1){
            assertThat(notes[i].color).isGreaterThan(notes[i+1].color)
        }
    }

}