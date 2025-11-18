package com.example.rocnikova_prace

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainScreenViewModel: ViewModel() {
    var currentIndex by mutableIntStateOf(0)
        private set

    fun updateCurrentIndex(newIndex: Int) {
        currentIndex = newIndex
    }
}