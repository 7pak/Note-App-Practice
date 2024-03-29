package com.example.noteapppractice.feature_note.presentation.notes

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteapppractice.MainActivity
import com.example.noteapppractice.core.constants.TestTags
import com.example.noteapppractice.di.AppModule
import com.example.noteapppractice.feature_note.presentation.util.Screens
import com.example.noteapppractice.ui.theme.NoteAppPracticeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest{

    @get:Rule(order = 0) //to start this rule first
    val hiltRule = HiltAndroidRule(this) //to use this dagger tester

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>() // to make sure we only use our main activity

    @Before
    fun setUp(){
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NoteAppPracticeTheme {
                NavHost(navController = navController, startDestination = Screens.NotesScreen.route){
                    composable(route = Screens.NotesScreen.route){
                        NotesScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun clickToggleOrderSection_isVisible(){
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Sort").performClick()
        composeRule.onNodeWithTag(TestTags.ORDER_SECTION).assertIsDisplayed()
    }
}