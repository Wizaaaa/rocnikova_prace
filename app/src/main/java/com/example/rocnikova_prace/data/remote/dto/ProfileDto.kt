package com.example.rocnikova_prace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto (
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val email: String,
    @SerialName("fcm_token")
    val fcmToken: String? = null,
    @SerialName("notifications_enabled")
    val notificationsEnabled: Boolean = true
)