package com.example.rocnikova_prace.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "result",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("groupId")]
)
data class ResultEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val userId: String,
    val percentage: Float,
    val createdAt: Long = System.currentTimeMillis()
)