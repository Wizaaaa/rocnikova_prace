package com.example.rocnikova_prace.ui.screens.questionsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.components.DeleteDialog
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.XMark

@Composable
fun QuestionsScreen(
    viewModel: GroupsViewModel,
    navController: NavHostController,
    onGroupClick: (String) -> Unit
) {
    val (groupToDelete, setGroupToDelete) = remember { mutableStateOf<GroupEntity?>(null) }

    when {
        viewModel.isLoading -> {
//            TODO
        }

        viewModel.groups.isEmpty() -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.QS_not_existing_group_questions),
                    style = MaterialTheme.typography.titleMedium
                )

                Cards(
                    text = R.string.CS_new_questions,
                    icon = Heroicons.Outline.Plus,
                    onClick = {
                        val newId = java.util.UUID.randomUUID().toString()
                        navController.navigate("${MainScreen.CreateInformation.name}/$newId")
                    },
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        else -> {
            LazyColumn(modifier = Modifier.safeDrawingPadding()) {
                items(
                    items = viewModel.groups,
                    key = { group -> group.id }
                ) { group ->
                    Box {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onGroupClick(group.id) }
                        ) {
                            Text(
                                text = group.name,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        Icon(
                            imageVector = Heroicons.Outline.XMark,
                            contentDescription = "delete questions",
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 12.dp, end = 12.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable {
                                    setGroupToDelete(group)
                                }
                        )
                    }
                }
            }
            if (groupToDelete != null) {
                DeleteDialog(
                    onDismissRequest = {
                        setGroupToDelete(null)
                    },
                    onConfirmation = {
                        viewModel.deleteGroup(groupToDelete)
                        setGroupToDelete(null)
                    }
                )
            }
        }
    }
}