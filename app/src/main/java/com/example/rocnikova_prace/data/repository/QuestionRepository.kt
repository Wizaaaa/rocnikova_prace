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
        val localData = groupDao.getAllGroups().first()
        emit(localData)

        try {
            val remoteDto = supabase.from("question_groups")
                .select()
                .decodeList<GroupDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            groupDao.refreshGroups(remoteEntities)

            val updatedData = groupDao.getAllGroups().first()
            emit(updatedData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getGroupById(id: String): GroupEntity? {
        return try {
            val remoteDto = supabase.from("question_groups")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingle<GroupDto>()

            val entity = remoteDto.toEntity()

            groupDao.insertGroup(entity)

            entity
        } catch (e: Exception) {
            e.printStackTrace()
            return groupDao.getGroupById(id)
        }
    }

    suspend fun getQuestionsOnce(groupId: String): List<QuestionEntity> {
        return try {
            val remoteDto = supabase.from("questions")
                .select {
                    filter {
                        eq("group_id", groupId)
                    }
                }
                .decodeList<QuestionDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            questionDao.refreshQuestions(groupId, remoteEntities)

            remoteEntities
        } catch (e: Exception) {
            e.printStackTrace()
            return questionDao.getQuestionsForPractice(groupId)
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
        val localData = resultDao.getAllForGroup(groupId).first()
        emit(localData)

        try {
            val remoteDto = supabase.from("result")
                .select {
                    filter {
                        eq("group_id", groupId)
                    }
                }
                .decodeList<ResultDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            resultDao.refreshResult(groupId, remoteEntities)

            val updatedData = resultDao.getAllForGroup(groupId).first()
            emit(updatedData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getQuestionsForGroup(groupId: String): Flow<List<QuestionItem>> = flow {
        val localData = questionDao.getQuestionsForGroup(groupId).first()
        emit(localData.map { it.toQuestionItem() })

        try {
            val remoteDto = supabase.from("questions")
                .select {
                    filter {
                        eq("group_id", groupId)
                    }
                }
                .decodeList<QuestionDto>()

            val remoteEntities = remoteDto.map { it.toEntity() }

            questionDao.refreshQuestions(groupId, remoteEntities)

            val updatedData = questionDao.getQuestionsForGroup(groupId).first()
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
        val localResults = resultDao.getAllResults().first()
        val localGroups = groupDao.getAllGroups().first()

        emit(calculateSummaries(localResults, localGroups))

        try {
            val remoteResultDto = supabase.from("result")
                .select()
                .decodeList<ResultDto>()

            val remoteResultEntities = remoteResultDto.map { it.toEntity() }
            resultDao.insertAll(remoteResultEntities)

            val updatedResults = resultDao.getAllResults().first()
            val updatedGroups = groupDao.getAllGroups().first()

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