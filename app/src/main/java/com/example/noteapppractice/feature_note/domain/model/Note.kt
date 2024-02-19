package com.example.noteapppractice.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapppractice.ui.theme.BabyBlue
import com.example.noteapppractice.ui.theme.LightGreen
import com.example.noteapppractice.ui.theme.RedOrange
import com.example.noteapppractice.ui.theme.RedPink
import com.example.noteapppractice.ui.theme.Violet

@Entity
data class Note(
    val title:String,
    val content:String,
    val color: Int,
    val timestamp:Long,
    @PrimaryKey
    val id:Int?=null
){
    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message:String):Exception(message)
