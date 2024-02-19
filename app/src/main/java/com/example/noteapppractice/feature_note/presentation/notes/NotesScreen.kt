package com.example.noteapppractice.feature_note.presentation.notes

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.noteapppractice.core.constants.TestTags
import com.example.noteapppractice.feature_note.presentation.notes.components.NoteItem
import com.example.noteapppractice.feature_note.presentation.notes.components.OrderSection
import com.example.noteapppractice.feature_note.presentation.util.Screens
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun NotesScreen(
    navController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                   navController.navigate(Screens.AddEditNoteScreen.route)
            }, backgroundColor = MaterialTheme.colors.primary) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            Modifier

                .fillMaxSize()
                .padding(16.dp)
                .padding(it)
        ) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "My Notes", style = MaterialTheme.typography.h4)
                
                IconButton(onClick = {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription ="Sort")
                }
            }

            AnimatedVisibility(visible = state.isOrderSectionVisible, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
                OrderSection(modifier = Modifier
                    .testTag(TestTags.ORDER_SECTION)
                    .fillMaxWidth()
                    .padding(16.dp), noteOrder = state.noteOrder){noteOrder->
                    viewModel.onEvent(NotesEvent.Order(noteOrder))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(modifier = Modifier.fillMaxSize()){
                items(state.notes){note->
                    
                    NoteItem(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screens.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                        },note = note){
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                        scope.launch {
                          val result =  scaffoldState.snackbarHostState.showSnackbar(
                                message = "Note deleted",
                                actionLabel = "Undo",
                            )

                            if (result == SnackbarResult.ActionPerformed){
                                viewModel.onEvent(NotesEvent.RestoreNote)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

            }
        }
    }

}