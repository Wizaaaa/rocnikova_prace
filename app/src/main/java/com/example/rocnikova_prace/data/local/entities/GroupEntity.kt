package com.example.rocnikova_prace.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "question_groups")
data class GroupEntity (
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)