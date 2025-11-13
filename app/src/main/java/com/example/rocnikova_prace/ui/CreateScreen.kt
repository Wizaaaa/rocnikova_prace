package com.example.rocnikova_prace.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.components.ClickCard
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.AcademicCap
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowDownTray
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus

@Composable
fun CreateScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var isClicked by remember { mutableStateOf(false) }

        Cards(
            text = R.string.new_questions,
            icon = Heroicons.Outline.Plus,
            isClicked = isClicked,
            onClick = {
                isClicked = !isClicked
                showDialog = !showDialog
            }
        )

        Cards(
            text = R.string.schools_questions,
            icon = Heroicons.Outline.AcademicCap,
            isClicked = isClicked,
            onClick = {
                isClicked = !isClicked
                showDialog = !showDialog
            }
        )

        Cards(
            text = R.string.import_questions,
            icon = Heroicons.Outline.ArrowDownTray,
            isClicked = isClicked,
            onClick = {
                isClicked = !isClicked
                showDialog = !showDialog
            }
        )

        if (showDialog) {
            ClickCard(onDismissRequest = { onDismissRequest() })
        }

    }
}

private fun onDismissRequest() {

}

@Preview
@Composable
fun CreateScreenPreview() {
    CreateScreen()
}