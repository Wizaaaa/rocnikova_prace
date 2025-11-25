package com.example.rocnikova_prace.data.model

data class QuestionUiData(
    val question: String,
    val options: List<String> = emptyList(),
    val correctIndex: Int? = null,
    val correctIndices: List<Int> = emptyList(),
    val answer: String? = null
)
