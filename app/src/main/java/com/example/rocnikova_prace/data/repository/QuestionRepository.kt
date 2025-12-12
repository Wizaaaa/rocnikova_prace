package com.example.rocnikova_prace.data.repository

import com.example.rocnikova_prace.data.local.QuestionDao
import com.example.rocnikova_prace.data.local.toEntity
import com.example.rocnikova_prace.data.local.toQuestionItem
import com.example.rocnikova_prace.data.model.QuestionItem

class QuestionRepository(private val dao: QuestionDao) {

    suspend fun save(item: QuestionItem) {
        dao.insert(item.toEntity())
    }

    suspend fun loadAll(): List<QuestionItem> {
        return dao.getAll().map { it.toQuestionItem() }
    }

    suspend fun delete(item: QuestionItem) {
        dao.delete(item.toEntity())
    }
}