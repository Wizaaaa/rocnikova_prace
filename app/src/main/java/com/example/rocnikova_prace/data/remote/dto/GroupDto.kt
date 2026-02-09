package com.example.rocnikova_prace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto (
    val id: String? = null,
    val name: String,
    val description: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)