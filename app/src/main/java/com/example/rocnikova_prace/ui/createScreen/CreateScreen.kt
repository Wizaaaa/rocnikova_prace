package com.example.rocnikova_prace.ui.createScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.components.ClickCard
import com.example.rocnikova_prace.ui.createInformation.CreateInformationViewModel
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.AcademicCap
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowDownTray
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus

@Composable
fun CreateScreen(
    navController: NavController,
    viewModel: CreateScreenViewModel = viewModel(),
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
            ClickCard(
                onDismissRequest = {
                    viewModel.onDismissRequest()
                    createInformationViewModel.groupNameReset()
                },
                onConfirm = {
                    viewModel.onConfirm()
                    navController.navigate(MainScreen.CreateInformation.name)
                },
                viewModel = createInformationViewModel
            )
        }
    }
}

@Preview
@Composable
fun CreateScreenPreview() {
    CreateScreen(navController = rememberNavController(), createInformationViewModel = viewModel())
}