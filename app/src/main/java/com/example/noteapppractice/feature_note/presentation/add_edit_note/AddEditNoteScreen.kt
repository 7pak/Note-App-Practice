package com.example.noteapppractice.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.noteapppractice.core.constants.TestTags
import com.example.noteapppractice.feature_note.domain.model.Note
import com.example.noteapppractice.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavHostController,
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    noteColor: Int
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()
    val noteBackgroundColor = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)

        )
    }
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {event->
            when (event) {
                is  AddEditNoteViewModel.UiEvent.SaveNote -> {
                   navController.navigateUp()
               }
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }, backgroundColor = MaterialTheme.colors.primary) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }
        }, scaffoldState = scaffoldState
    ) { paddingValues->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundColor.value)
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt =
                        color.toArgb()  // To convert the color to int because those colors are compose colors
                    Box(modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .shadow(16.dp, CircleShape)
                        .background(color)
                        .border(
                            width = 3.dp,
                            shape = CircleShape,
                            color = if (viewModel.noteColor.value == colorInt)
                                Color.Black else Color.Transparent
                        )
                        .clickable {
                            scope.launch {
                                noteBackgroundColor.animateTo(
                                    targetValue = Color(colorInt), animationSpec = tween(
                                        durationMillis = 500
                                    )
                                )
                            }
                            viewModel.onEvent(AddEditNoteEvent.EnteredColor(colorInt))
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(focusState = it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                testTag = TestTags.TITLE_TEXT_FIELD
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(focusState = it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight(),
                testTag = TestTags.CONTENT_TEXT_FIELD
            )

        }
    }
}