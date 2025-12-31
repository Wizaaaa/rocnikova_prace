package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R

@Composable
fun InformationCard(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(10.dp),
        label = {
          Text(label)
        },
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun InformationCardPreview() {
    InformationCard("", { }, stringResource(R.string.new_questions_group))
}