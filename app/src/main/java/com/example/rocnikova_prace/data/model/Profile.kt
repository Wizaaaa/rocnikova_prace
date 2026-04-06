package com.example.rocnikova_prace.data.model

data class Profile(
    val id: String,
    val userName: String,
    val email: String,
    val avatarUrl: String?,
    val notificationsEnabled: Boolean
)