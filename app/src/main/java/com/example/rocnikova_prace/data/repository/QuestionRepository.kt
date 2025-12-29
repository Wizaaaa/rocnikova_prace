package com.example.rocnikova_prace.data.repository

import com.example.rocnikova_prace.data.local.dao.GroupDao
import com.example.rocnikova_prace.data.local.dao.QuestionDao
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.local.toEntity
import com.example.rocnikova_prace.data.local.toQuestionItem
import com.example.rocnikova_prace.data.model.QuestionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionRepository(
    private val questionDao: QuestionDao,
    private val groupDao: GroupDao
) {
    fun getAllGroups(): Flow<List<GroupEntity>> {
        return groupDao.getAllGroups()
    }

    suspend fun getGroupById(id: String): GroupEntity? {
        return groupDao.getGroupById(id)
    }

    suspend fun saveGroup(group: GroupEntity) {
        groupDao.insertGroup(group)
    }

    suspend fun deleteGroup(group: GroupEntity) {
        groupDao.deleteGroup(group)
    }

    fun getQuestionsForGroup(groupId: String): Flow<List<QuestionItem>> {
        return questionDao.getQuestionsForGroup(groupId).map { entities ->
            entities.map { it.toQuestionItem() }
        }
    }

    suspend fun saveQuestion(item: QuestionItem) {
        questionDao.insert(item.toEntity())
    }

    suspend fun deleteQuestion(item: QuestionItem) {
        questionDao.delete(item.toEntity())
    }
}