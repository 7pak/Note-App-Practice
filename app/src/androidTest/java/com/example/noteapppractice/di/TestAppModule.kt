package com.example.noteapppractice.di

import android.app.Application
import androidx.room.Room
import com.example.noteapppractice.feature_note.data.data_source.NoteDatabase
import com.example.noteapppractice.feature_note.data.repository.NoteRepositoryImp
import com.example.noteapppractice.feature_note.domain.repository.NoteRepository
import com.example.noteapppractice.feature_note.domain.use_case.AddNoteUseCase
import com.example.noteapppractice.feature_note.domain.use_case.DeleteNoteUseCase
import com.example.noteapppractice.feature_note.domain.use_case.GetNoteUseCase
import com.example.noteapppractice.feature_note.domain.use_case.GetNotesUseCase
import com.example.noteapppractice.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(
            context = app,
            klass = NoteDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDatabase): NoteRepository {
        return NoteRepositoryImp(dao.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(noteRepository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotesUseCase(noteRepository),
            getNote = GetNoteUseCase(noteRepository),
            deleteNote = DeleteNoteUseCase(noteRepository),
            addNote = AddNoteUseCase(noteRepository)
        )
    }

}