package com.example.noteapppractice.feature_note.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapppractice.MainActivity
import com.example.noteapppractice.core.constants.TestTags
import com.example.noteapppractice.di.AppModule
import com.example.noteapppractice.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.noteapppractice.feature_note.presentation.notes.NotesScreen
import com.example.noteapppractice.feature_note.presentation.util.Screens
import com.example.noteapppractice.ui.theme.NoteAppPracticeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NoteEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            NoteAppPracticeTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.NotesScreen.route
                ) {
                    composable(route = Screens.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(route = Screens.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(name = "noteId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }, navArgument("noteColor") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )) { entries ->
                        val color = entries.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(navController = navController, noteColor = color)
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_EditAfterwards() {
        //Click on FAB to add new note
        composeRule.onNodeWithContentDescription("add note").performClick()

        //Add title and content to the note fields
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("test-title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).
        performTextInput("test-content")

        //Save the note
        composeRule.onNodeWithContentDescription("Save note")
            .performClick()

        //Check for the note we added
        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        //Click on the note we added
        composeRule.onNodeWithText("test-title").performClick()

        //Check if the note with the title and content are exist
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .assertTextEquals("test-title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .assertTextEquals("test-content")
        //Add content to the title
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("2")
        //Save the new edited note
        composeRule.onNodeWithContentDescription("Save note")
            .performClick()

        //Make sure the update was applied to the list
        composeRule.onNodeWithText("2test-title").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_OrderedByTitleDescending(){
        for (i in 1..3){
            //Click on FAB to add new note
            composeRule.onNodeWithContentDescription("add note").performClick()

            //Add title and content to the note fields
            composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
                .performTextInput("$i")
            composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).
            performTextInput("$i")
            //Save the note
            composeRule.onNodeWithContentDescription("Save note")
                .performClick()
        }
        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Sort").performClick()

        composeRule.onNodeWithContentDescription("Title").performClick()
        composeRule.onNodeWithContentDescription("Descending").performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1]
            .assertTextContains("2")
       composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2]
            .assertTextContains("1")


    }
}