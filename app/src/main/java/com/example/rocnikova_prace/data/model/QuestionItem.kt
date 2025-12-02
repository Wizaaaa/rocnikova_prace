package com.example.rocnikova_prace.data.model

import java.util.UUID

sealed class QuestionItem(
    val id: String = UUID.randomUUID().toString()
) {

    data class MultipleChoiceSingle(
        val question: String,
        val options: List<String>,
        val correctIndex: Int?
    ) : QuestionItem()

    data class MultipleChoiceMultiple(
        val question: String,
        val options: List<String>,
        val correctIndices: List<Int>
    ) : QuestionItem()

    data class Open(
        val question: String,
        val answer: String
    ) : QuestionItem()

    data class FillBlank(
        val question: String,
        val answer: String
    ) : QuestionItem()


    companion object {

        fun emptyMultipleChoiceSingle() = MultipleChoiceSingle(
            question = "",
            options = listOf("", "", "", ""),
            correctIndex = null
        )

        fun emptyMultipleChoiceMultiple() = MultipleChoiceMultiple(
            question = "",
            options = listOf("", "", "", ""),
            correctIndices = emptyList()
        )

        fun emptyOpen() = Open(
            question = "",
            answer = ""
        )

        fun emptyFillBlank() = FillBlank(
            question = "_____",
            answer = ""
        )


    }
}