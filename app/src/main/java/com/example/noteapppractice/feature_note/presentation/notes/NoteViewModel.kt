package com.example.noteapppractice.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapppractice.feature_note.domain.model.Note
import com.example.noteapppractice.feature_note.domain.use_case.NoteUseCases
import com.example.noteapppractice.feature_note.domain.util.NoteOrder
import com.example.noteapppractice.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val notesUseCase: NoteUseCases
) : ViewModel() {

    private var _state = mutableStateOf(NotesStates())
    val state: State<NotesStates> = _state

    private var recentDeletedNote: Note? = null

    private var getNoteJob:Job?=null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }


    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCase.deleteNote(event.note)
                    recentDeletedNote = event.note
                }
            }

            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.netOrder::class && state.value.noteOrder.orderType == event.netOrder.orderType) {
                    return
                }
                getNotes(event.netOrder)
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    notesUseCase.addNote(note = recentDeletedNote ?: return@launch)
                    recentDeletedNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }
    private fun getNotes(noteOrder:NoteOrder){
        getNoteJob?.cancel()
       getNoteJob =  notesUseCase.getNotes(noteOrder).onEach {
            _state.value = state.value.copy(
                notes = it,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}