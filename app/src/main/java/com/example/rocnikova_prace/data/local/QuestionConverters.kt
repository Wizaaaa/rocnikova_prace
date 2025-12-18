package com.example.rocnikova_prace.data.local

import com.example.rocnikova_prace.data.model.QuestionItem
import kotlinx.serialization.json.Json

private val jsonConfig = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun QuestionItem.toEntity(): QuestionEntity {
    val type = when (this) {
        is QuestionItem.MultipleChoice -> "multiple"
        is QuestionItem.Open -> "open"
        is QuestionItem.FillBlank -> "fillBlank"
    }

    return QuestionEntity(
        id = id,
        type = type,
        data = jsonConfig.encodeToString(this)
    )
}

fun QuestionEntity.toQuestionItem(): QuestionItem {
    return when (type) {
        "multiple" -> jsonConfig.decodeFromString<QuestionItem.MultipleChoice>(data)
        "open" -> jsonConfig.decodeFromString<QuestionItem.Open>(data)
        "fillBlank" -> jsonConfig.decodeFromString<QuestionItem.FillBlank>(data)
        else -> error("Unknown question type: $type")
    }
}