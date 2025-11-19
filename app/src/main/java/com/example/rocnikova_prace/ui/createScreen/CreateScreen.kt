package com.example.rocnikova_prace.ui.createScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.createInformation.CreateInformation
import com.example.rocnikova_prace.ui.createInformation.CreateInformationViewModel
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.AcademicCap
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowDownTray
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus

@Composable
fun CreateScreen(
    viewModel: CreateScreenViewModel,
    createInformationViewModel: CreateInformationViewModel
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Cards(
            text = R.string.CS_new_questions,
            icon = Heroicons.Outline.Plus,
            onClick = { viewModel.createCardOnClick() }
        )

        Cards(
            text = R.string.CS_schools_questions,
            icon = Heroicons.Outline.AcademicCap,
            onClick = {  }
        )

        Cards(
            text = R.string.CS_import_questions,
            icon = Heroicons.Outline.ArrowDownTray,
            onClick = {  }
        )

        if (viewModel.showDialog) {
            CreateInformation(
                createScreenViewModel = viewModel,
                viewModel = createInformationViewModel
            )
        }
    }
}

@Preview
@Composable
fun CreateScreenPreview() {
    CreateScreen(viewModel(), viewModel())
}