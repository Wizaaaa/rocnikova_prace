package com.example.rocnikova_prace.ui.screens.createScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.AcademicCap
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowDownTray
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus

@Composable
fun CreateScreen(
    navController: NavHostController
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Cards(
            text = R.string.CS_new_questions,
            icon = Heroicons.Outline.Plus,
            onClick = {
                val newId = java.util.UUID.randomUUID().toString()
                navController.navigate("${MainScreen.CreateInformation.name}/$newId")
            },
            modifier = Modifier.padding(20.dp)
        )

        Cards(
            text = R.string.CS_schools_questions,
            icon = Heroicons.Outline.AcademicCap,
            onClick = {  },
            modifier = Modifier.padding(20.dp)
        )

        Cards(
            text = R.string.CS_import_questions,
            icon = Heroicons.Outline.ArrowDownTray,
            onClick = {  },
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Preview
@Composable
fun CreateScreenPreview() {
    CreateScreen(rememberNavController())
}