package com.example.rocnikova_prace.data.repository

import com.example.rocnikova_prace.data.local.dao.GroupDao
import com.example.rocnikova_prace.data.local.dao.QuestionDao
import com.example.rocnikova_prace.data.local.dao.ResultDao
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.local.entities.QuestionEntity
import com.example.rocnikova_prace.data.local.entities.ResultEntity
import com.example.rocnikova_prace.data.local.toEntity
import com.example.rocnikova_prace.data.local.toQuestionItem
import com.example.rocnikova_prace.data.mappers.toDto
import com.example.rocnikova_prace.data.mappers.toEntity
import com.example.rocnikova_prace.data.model.GroupSummary
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.data.remote.dto.GroupDto
import com.example.rocnikova_prace.data.remote.dto.QuestionDto
import com.example.rocnikova_prace.data.remote.dto.ResultDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class QuestionRepository(
    private val questionDao: QuestionDao,
    private val groupDao: GroupDao,
    private val resultDao: ResultDao,
    private val supabase: io.github.jan.supabase.SupabaseClient = SupabaseClient.client
) {
    fun getAllGroups(): Flow<List<GroupEntity>> = flow {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return@flow
        val localData = groupDao.getAllGroups(currentUserId).first()
        emit(localData)

        try {
            val remoteDto = supabase.from("question_groups")
                .select{
                    filter {
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<GroupDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            groupDao.refreshGroups(currentUserId, remoteEntities)

            val updatedData = groupDao.getAllGroups(currentUserId).first()
            emit(updatedData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getGroupById(id: String): GroupEntity? {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return null
        return try {
            val remoteDto = supabase.from("question_groups")
                .select {
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<GroupDto>()

            val entity = remoteDto.toEntity()

            groupDao.insertGroup(entity)

            entity
        } catch (e: Exception) {
            e.printStackTrace()
            groupDao.getGroupById(id, currentUserId)
        }
    }

    suspend fun getQuestionsOnce(groupId: String): List<QuestionEntity> {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return emptyList()
        return try {
            val remoteDto = supabase.from("questions")
                .select {
                    filter {
                        eq("group_id", groupId)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<QuestionDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            questionDao.refreshQuestions(groupId, currentUserId, remoteEntities)

            remoteEntities
        } catch (e: Exception) {
            e.printStackTrace()
            questionDao.getQuestionsForPractice(groupId, currentUserId)
        }
    }

    suspend fun deleteGroup(group: GroupEntity) {
        try {
            supabase.from("question_groups").delete {
                filter { eq("id", group.id) }
            }
            groupDao.deleteGroup(group)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    fun getTestResultsStream(groupId: String): Flow<List<ResultEntity>> = flow {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return@flow
        val localData = resultDao.getAllForGroup(groupId, currentUserId).first()
        emit(localData)

        try {
            val remoteDto = supabase.from("result")
                .select {
                    filter {
                        eq("group_id", groupId)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<ResultDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            resultDao.refreshResult(groupId, currentUserId, remoteEntities)

            val updatedData = resultDao.getAllForGroup(groupId, currentUserId).first()
            emit(updatedData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getQuestionsForGroup(groupId: String): Flow<List<QuestionItem>> = flow {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return@flow
        val localData = questionDao.getQuestionsForGroup(groupId, currentUserId).first()
        emit(localData.map { it.toQuestionItem() })

        try {
            val remoteDto = supabase.from("questions")
                .select {
                    filter {
                        eq("group_id", groupId)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<QuestionDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            questionDao.refreshQuestions(groupId, currentUserId, remoteEntities)

            val updatedData = questionDao.getQuestionsForGroup(groupId, currentUserId).first()
            emit(updatedData.map { it.toQuestionItem() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveGroup(group: GroupEntity) {
        try {
            val dto = group.toDto()
            supabase.from("question_groups").upsert(dto)

            groupDao.insertGroup(group)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


    suspend fun saveQuestion(item: QuestionItem) {
        try {
            val entity = item.toEntity()
            val dto = entity.toDto()
            supabase.from("questions").upsert(dto)

            questionDao.insert(entity)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteQuestion(item: QuestionItem) {
        try {
            val entity = item.toEntity()

            supabase.from("questions").delete {
                filter { eq("id", entity.id) }
            }
            questionDao.delete(entity)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


    suspend fun saveTestResult(groupId: String, percentage: Float) {
        val newId = java.util.UUID.randomUUID().toString()
        val currentUserId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("Uživatel není přihlášen!")

        val resultEntity = ResultEntity(
            id = newId,
            groupId = groupId,
            userId = currentUserId,
            percentage = percentage
        )

        try {
            val dto = resultEntity.toDto()
            supabase.from("result").upsert(dto)
            resultDao.insert(resultEntity)
        } catch (e: Exception) {
            e.printStackTrace()
            resultDao.insert(resultEntity)
        }
    }


    fun getGroupsOverviewStream(): Flow<List<GroupSummary>> = flow {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return@flow
        val localResults = resultDao.getAllResults(currentUserId).first()
        val localGroups = groupDao.getAllGroups(currentUserId).first()

        emit(calculateSummaries(localResults, localGroups))

        try {
            val remoteResultDto = supabase.from("result")
                .select{
                    filter {
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<ResultDto>()

            val remoteResultEntities = remoteResultDto.map { it.toEntity() }
            resultDao.insertAll(remoteResultEntities)

            val updatedResults = resultDao.getAllResults(currentUserId).first()
            val updatedGroups = groupDao.getAllGroups(currentUserId).first()

            emit(calculateSummaries(updatedResults, updatedGroups))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun calculateSummaries(
    results: List<ResultEntity>,
    groups: List<GroupEntity>
): List<GroupSummary> {
    val groupMap = groups.associateBy { it.id }

    val groupedMap = results.groupBy { it.groupId }

    return groupedMap.mapNotNull { (groupId, resultsOfGroup) ->
        val average = resultsOfGroup.map { it.percentage }.average()

        val groupName = groupMap[groupId]?.name ?: "Neznámá skupina"

        GroupSummary(
            groupId = groupId,
            groupName = groupName,
            averageScore = average.toFloat(),
            totalAttempts = resultsOfGroup.size
        )
    }
}