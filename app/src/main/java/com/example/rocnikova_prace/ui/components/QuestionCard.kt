package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel

@Composable
fun QuestionCard(
    question: QuestionItem.MultipleChoice,
    viewModel: CreateInformationViewModel,
    questionIndex: Int,
    isCheckboxError: Boolean,
    modifier: Modifier = Modifier
) {
    var text by remember(question.id, questionIndex) {
        mutableStateOf(question.options[questionIndex])
    }

    val isTextError = viewModel.showErrors && text.isBlank()

    val correctIndices = question.correctIndices.toMutableList()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 10.dp)
    ) {
        InformationCard(
            value = text,
            onValueChange = {
                text = it

                val newOptions = question.options.toMutableList()
                newOptions[questionIndex] = it
                viewModel.updateQuestion(
                    updated = question.copy(options = newOptions),
                )
            },
            isError = isTextError,
            supportingText = {
                if (isTextError) {
                    Text(
                        text = stringResource(R.string.QC_option_required),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = stringResource(R.string.enter_response),
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)
                .shake(isTextError, trigger = viewModel.validationErrorTrigger)
        )

        Checkbox(
            checked = (question.correctIndices[questionIndex]),
            onCheckedChange = {
                correctIndices[questionIndex] = !correctIndices[questionIndex]
                viewModel.updateQuestion(
                    updated = question.copy(correctIndices = correctIndices),
                )
            },
            colors = if (isCheckboxError) CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.error) else CheckboxDefaults.colors(),
            modifier = Modifier.shake(isCheckboxError, trigger = viewModel.validationErrorTrigger)
        )
    }
}