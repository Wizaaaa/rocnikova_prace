package com.example.rocnikova_prace.data.local

import com.example.rocnikova_prace.data.local.entities.QuestionEntity
import com.example.rocnikova_prace.data.model.QuestionItem
import kotlinx.serialization.json.Json

private val jsonConfig = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    coerceInputValues = true
}

fun QuestionItem.toEntity(): QuestionEntity {
    val type = when (this) {
        is QuestionItem.MultipleChoice -> "multiple"
        is QuestionItem.Open -> "open"
        is QuestionItem.FillBlank -> "fillBlank"
    }

    val jsonData = when (this) {
        is QuestionItem.MultipleChoice -> jsonConfig.encodeToString(this)
        is QuestionItem.Open -> jsonConfig.encodeToString(this)
        is QuestionItem.FillBlank -> jsonConfig.encodeToString(this)
    }

    return QuestionEntity(
        id = id,
        groupId = groupId,
        type = type,
        data = jsonData
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