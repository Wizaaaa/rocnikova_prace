package com.example.rocnikova_prace.data.model

import java.util.UUID

sealed class QuestionItem {
    abstract val id: String
    abstract val isExpanded: Boolean

    fun changeExpanded(expanded: Boolean): QuestionItem {
        return when (this) {
            is MultipleChoice -> this.copy(isExpanded = expanded)
            is Open -> this.copy(isExpanded = expanded)
            is FillBlank -> this.copy(isExpanded = expanded)
        }
    }

    data class MultipleChoice(
        val question: String,
        val options: List<String>,
        val correctIndices: List<Boolean>,
        override var isExpanded: Boolean = false,
        override val id: String = UUID.randomUUID().toString()
    ) : QuestionItem()

    data class Open(
        val question: String,
        val answer: String,
        override var isExpanded: Boolean = false,
        override val id: String = UUID.randomUUID().toString()
    ) : QuestionItem()

    data class FillBlank(
        val question: String,
        val answer: String,
        override var isExpanded: Boolean = false,
        override val id: String = UUID.randomUUID().toString()
    ) : QuestionItem()


    companion object {
        fun emptyMultipleChoice(id: String = UUID.randomUUID().toString()) = MultipleChoice(
            id = id,
            question = "",
            options = listOf("", "", "", ""),
            correctIndices = listOf(false, false, false, false)
        )

        fun emptyOpen(id: String = UUID.randomUUID().toString()) = Open(
            id = id,
            question = "",
            answer = ""
        )

        fun emptyFillBlank(id: String = UUID.randomUUID().toString()) = FillBlank(
            id = id,
            question = "_____",
            answer = ""
        )
    }
}