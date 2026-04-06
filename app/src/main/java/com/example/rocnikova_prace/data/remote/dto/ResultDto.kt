package com.example.rocnikova_prace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val id: String? = null,

    @SerialName("group_id")
    val groupId: String,

    @SerialName("user_id")
    val userId: String,

    val percentage: Float,

    @SerialName("created_at")
    val createdAt: String? = null
)
