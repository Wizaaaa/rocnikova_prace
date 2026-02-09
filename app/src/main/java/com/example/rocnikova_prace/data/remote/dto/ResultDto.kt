package com.example.rocnikova_prace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val id: String? = null,

    @SerialName("group_id")
    val groupId: String,

    val percentage: Float
)
