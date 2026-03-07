package com.example.rocnikova_prace.ui.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    saveButton: (() -> Unit)? = null
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(
                onClick = { backDispatcher?.onBackPressed() }
            ) {
                Icon(
                    imageVector = Heroicons.Outline.ArrowLeft,
                    contentDescription = "Navigate back"
                )
            }
        },
        actions = {
            if (saveButton != null) {
                TextButton(
                    onClick = { saveButton() }
                ) {
                    Text(
                        text = "Save"
                    )
                }
            }
        }
    )
}