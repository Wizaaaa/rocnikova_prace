package com.example.rocnikova_prace.data.mappers

import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.local.entities.QuestionEntity
import com.example.rocnikova_prace.data.local.entities.ResultEntity
import com.example.rocnikova_prace.data.remote.dto.GroupDto
import com.example.rocnikova_prace.data.remote.dto.QuestionDto
import com.example.rocnikova_prace.data.remote.dto.ResultDto

fun GroupDto.toEntity(): GroupEntity {
    return GroupEntity(
        id = this.id ?: java.util.UUID.randomUUID().toString(),
        userId = this.userId,
        name = this.name,
        description = this.description,
        isGlobal = this.isGlobal,
        createdAt = try {
            java.time.Instant.parse(this.createdAt).toEpochMilli()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    )
}

fun QuestionDto.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = this.id ?: java.util.UUID.randomUUID().toString(),
        groupId = this.groupId,
        userId = this.userId,
        type = this.type,
        data = this.data
    )
}

fun ResultDto.toEntity(): ResultEntity {
    return ResultEntity(
        id = this.id ?: java.util.UUID.randomUUID().toString(),
        groupId = this.groupId,
        userId = this.userId,
        percentage = this.percentage,
        createdAt = try {
            java.time.Instant.parse(this.createdAt).toEpochMilli()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    )
}


fun GroupEntity.toDto(): GroupDto {
    return GroupDto(
        id = this.id,
        userId = this.userId,
        name = this.name,
        description = this.description,
        isGlobal = this.isGlobal,
        createdAt = java.time.Instant.ofEpochMilli(this.createdAt).toString()
    )
}

fun QuestionEntity.toDto(): QuestionDto {
    return QuestionDto(
        id = this.id,
        groupId = this.groupId,
        userId = this.userId,
        type = this.type,
        data = this.data
    )
}

fun ResultEntity.toDto(): ResultDto {
    return ResultDto(
        id = this.id,
        groupId = this.groupId,
        userId = this.userId,
        percentage = this.percentage,
        createdAt = java.time.Instant.ofEpochMilli(this.createdAt).toString()
    )
}