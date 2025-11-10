package com.example.rocnikova_prace.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
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
        Cards(
            text = R.string.new_questions,
            icon = Heroicons.Outline.Plus
        )

        Cards(
            text = R.string.schools_questions,
            icon = Heroicons.Outline.AcademicCap
        )

        Cards(
            text = R.string.import_questions,
            icon = Heroicons.Outline.ArrowDownTray
        )

    }

}

@Preview
@Composable
fun CreateScreenPreview() {
    CreateScreen()
}