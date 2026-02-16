package com.example.rocnikova_prace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val id: String? = null, // database can generate null

    @SerialName("group_id")
    val groupId: String,

    @SerialName("user_id")
    val userId: String,

    val type: String,
    val data: String
)