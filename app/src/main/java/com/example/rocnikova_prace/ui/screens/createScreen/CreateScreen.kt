package com.example.rocnikova_prace.ui.screens.createScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.QuizViewModel
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.Cards
import com.example.rocnikova_prace.ui.screens.authScreen.AuthViewModel
import com.example.rocnikova_prace.ui.theme.Dimens
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.AcademicCap
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ArrowDownTray
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus

@Composable
fun CreateScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    quizViewModel: QuizViewModel,
    onNavigateToQuestions: () -> Unit = {}
) {
    val isLoading by quizViewModel.isLoading.collectAsState()
    val showImportDialog = remember { mutableStateOf(false) }
    val showSchoolDialog = remember { mutableStateOf(false) }
    val showSubjectDialog = remember { mutableStateOf(false) }
    var userNotes by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authViewModel.saveDeviceToken()
    }

    if (isLoading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(R.string.CS_dialog_loading),
                modifier = Modifier.padding(top = Dimens.medium)
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.large)
        ) {
            Cards(
                text = R.string.CS_new_questions,
                icon = Heroicons.Outline.Plus,
                onClick = {
                    val newId = java.util.UUID.randomUUID().toString()
                    navController.navigate("${MainScreen.CreateInformation.name}/$newId")
                }
            )

            Cards(
                text = R.string.CS_schools_questions,
                icon = Heroicons.Outline.AcademicCap,
                onClick = { showSchoolDialog.value = true }
            )

            Cards(
                text = R.string.CS_import_questions,
                icon = Heroicons.Outline.ArrowDownTray,
                onClick = { showImportDialog.value = true }
            )
        }
    }

    if (showSchoolDialog.value) {
        AlertDialog(
            onDismissRequest = { showSchoolDialog.value = false },
            title = { Text(stringResource(R.string.CS_school_dialog_title)) },
            text = {
                TextButton(
                    onClick = {
                        showSchoolDialog.value = false
                        showSubjectDialog.value = true
                    }
                ) {
                    Text(stringResource(R.string.CS_school_spse_mb))
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSchoolDialog.value = false }) {
                    Text(stringResource(R.string.CS_dialog_cancel))
                }
            }
        )
    }

    if (showSubjectDialog.value) {
        AlertDialog(
            onDismissRequest = { showSubjectDialog.value = false },
            title = { Text(stringResource(R.string.CS_subject_dialog_title)) },
            text = {
                TextButton(
                    onClick = {
                        showSubjectDialog.value = false
                        quizViewModel.importSchoolQuestions(
                            onSuccess = { onNavigateToQuestions() }
                        )
                    }
                ) {
                    Text(stringResource(R.string.CS_subject_programming))
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSubjectDialog.value = false }) {
                    Text(stringResource(R.string.CS_dialog_cancel))
                }
            }
        )
    }

    if (showImportDialog.value) {
        AlertDialog(
            onDismissRequest = { showImportDialog.value = false },
            title = { Text(stringResource(R.string.CS_dialog_title)) },
            text = {
                OutlinedTextField(
                    value = userNotes,
                    onValueChange = { userNotes = it },
                    label = { Text(stringResource(R.string.CS_dialog_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    maxLines = 10
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (userNotes.isNotBlank()) {
                            quizViewModel.createQuizNotes(userNotes, onSuccess = {
                                onNavigateToQuestions()
                            })
                            showImportDialog.value = false
                            userNotes = ""
                        }
                    }
                ) {
                    Text(stringResource(R.string.CS_dialog_submit))
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog.value = false }) {
                    Text(stringResource(R.string.CS_dialog_cancel))
                }
            }
        )
    }
}