package com.example.rocnikova_prace.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowLeft

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavController,
    viewmodel: CreateInformationViewModel
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.new_questions_group)) },
        navigationIcon = {
            IconButton(
                onClick = {
                    viewmodel.saveInformation (
                        onSuccess = { navController.popBackStack() }
                    )
                }
            ) {
                Icon(
                    imageVector = Heroicons.Outline.ArrowLeft,
                    contentDescription = "Navigate back "
                )
            }
        }
    )
}