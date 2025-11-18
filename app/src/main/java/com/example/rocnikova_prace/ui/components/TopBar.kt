package com.example.rocnikova_prace.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.createInformation.CreateInformationViewModel
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigateBack: () -> Unit,
    createInformationViewModel: CreateInformationViewModel,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(R.string.CCC_heading))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navigateBack()
                    createInformationViewModel.groupNameReset()
                }
            ) {
                Icon(
                    Heroicons.Outline.ArrowLeft,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar(navigateBack = {  }, viewModel())
}