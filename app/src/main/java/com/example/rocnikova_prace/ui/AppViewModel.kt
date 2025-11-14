package com.example.rocnikova_prace.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {
    var showDialog by mutableStateOf(false)
        private set

    var isClicked by mutableStateOf(false)
        private set

    fun onDismissRequest() {
        showDialog = false
    }

    fun onConfirm() {
        showDialog = true
    }

    fun createCardOnClick() {
        isClicked = !isClicked
        showDialog = !showDialog
    }
}