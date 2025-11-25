package com.example.rocnikova_prace.data.model

sealed class QuestionItem {

    data class MultipleChoiceSingle(
        val question: String,
        val options: List<String>,
        val correctIndex: Int
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

    fun toUiData(): QuestionUiData = when(this) {
        is MultipleChoiceSingle -> QuestionUiData(
            question = question,
            options = options,
            correctIndex = correctIndex
        )
        is MultipleChoiceMultiple -> QuestionUiData(
            question = question,
            options = options,
            correctIndices = correctIndices
        )
        is Open -> QuestionUiData(
            question = question,
            answer = answer
        )
        is FillBlank -> QuestionUiData(
            question = question,
            answer = answer
        )
    }

    companion object {

        fun emptyMultipleChoiceSingle() = MultipleChoiceSingle(
            question = "",
            options = listOf("", "", "", ""),
            correctIndex = 0
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