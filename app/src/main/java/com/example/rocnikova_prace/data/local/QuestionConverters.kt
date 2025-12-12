package com.example.rocnikova_prace.data.local

import com.example.rocnikova_prace.data.model.QuestionItem
import kotlinx.serialization.json.Json

fun QuestionItem.toEntity(): QuestionEntity {
    val type = when (this) {
        is QuestionItem.MultipleChoice -> "multiple"
        is QuestionItem.Open -> "open"
        is QuestionItem.FillBlank -> "fillBlank"
    }

    return QuestionEntity(
        id = id,
        type = type,
        data = Json.encodeToString(this)
    )
}

fun QuestionEntity.toQuestionItem(): QuestionItem {
    return when (type) {
        "multiple" -> Json.decodeFromString<QuestionItem.MultipleChoice>(data)
        "open" -> Json.decodeFromString<QuestionItem.Open>(data)
        "fillBlank" -> Json.decodeFromString<QuestionItem.FillBlank>(data)
        else -> error("Unknown question type")
    }
}