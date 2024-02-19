package com.example.noteapppractice.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun DefaultRadioButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelected: () -> Unit
) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier.semantics{
                this.contentDescription = text
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = text, style = MaterialTheme.typography.body1)
    }
}