package com.example.rocnikova_prace.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class QuestionItem {
    abstract val id: String
    abstract val isExpanded: Boolean
    abstract val groupId: String
    abstract val question: String
    abstract val userId: String

    fun changeExpanded(expanded: Boolean): QuestionItem {
        return when (this) {
            is MultipleChoice -> this.copy(isExpanded = expanded)
            is Open -> this.copy(isExpanded = expanded)
            is FillBlank -> this.copy(isExpanded = expanded)
        }
    }

    @Serializable
    data class MultipleChoice(
        val answers: List<String>,
        val correctIndices: List<Boolean>,
        override val question: String,
        override val groupId: String,
        override val userId: String,
        override var isExpanded: Boolean = false,
        override val id: String = UUID.randomUUID().toString()
    ) : QuestionItem()

    @Serializable
    data class Open(
        val answer: String,
        override val question: String,
        override val groupId: String,
        override val userId: String,
        override var isExpanded: Boolean = false,
        override val id: String = UUID.randomUUID().toString()
    ) : QuestionItem()

    @Serializable
    data class FillBlank(
        val answer: String,
        override val question: String,
        override val groupId: String,
        override val userId: String,
        override var isExpanded: Boolean = false,
        override val id: String = UUID.randomUUID().toString()
    ) : QuestionItem()


    companion object {
        fun emptyMultipleChoice(
            groupId: String,
            id: String = UUID.randomUUID().toString(),
            userId: String,
            question: String = ""
        ) = MultipleChoice(
            id = id,
            groupId = groupId,
            userId = userId,
            question = question,
            answers = listOf("", "", "", ""),
            correctIndices = listOf(false, false, false, false)
        )

        fun emptyOpen(
            groupId: String,
            id: String = UUID.randomUUID().toString(),
            userId: String,
            question: String = ""
        ) = Open(
            id = id,
            groupId = groupId,
            userId = userId,
            question = question,
            answer = ""
        )

        fun emptyFillBlank(
            groupId: String,
            id: String = UUID.randomUUID().toString(),
            userId: String,
            question: String = ""
        ) = FillBlank(
            id = id,
            groupId = groupId,
            userId = userId,
            question = question,
            answer = ""
        )
    }
}