package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionsScreen(
    viewModel: GroupsViewModel,
    onGroupClick: (String) -> Unit
) {
    when {
        viewModel.isLoading -> {
            Text("isLoading")
        }

        viewModel.groups.isEmpty() -> {
            Text("isEmpty")
        }

        else -> {
            LazyColumn(modifier = Modifier) {
                items(viewModel.groups) { group ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onGroupClick(group.id) }
                    ) {
                        Text(
                            text = group.name,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}