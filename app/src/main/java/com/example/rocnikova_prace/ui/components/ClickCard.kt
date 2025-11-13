package com.example.rocnikova_prace.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rocnikova_prace.ui.theme.AppTheme
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickCard(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        content = {
            Icon(Heroicons.Solid.User, null)

            TextField(
                value = name,
                onValueChange = { name = it }
            )
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

private fun onDismissRequest() {

}

@Preview
@Composable
fun ClickCardPreview() {
    AppTheme {
        ClickCard(onDismissRequest = { onDismissRequest() })
    }
}