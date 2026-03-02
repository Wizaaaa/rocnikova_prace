package com.example.rocnikova_prace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.rocnikova_prace.data.local.entities.ResultEntity
import com.example.rocnikova_prace.data.model.GroupSummary
import kotlinx.coroutines.flow.Flow
@Dao
interface ResultDao {
    @Insert
    suspend fun insert(result: ResultEntity)

    @Insert
    suspend fun insertAll(result: List<ResultEntity>)

    @Query("""
        SELECT 
            r.groupId AS groupId, 
            g.name AS groupName,
            COUNT(r.id) AS totalAttempts, 
            AVG(r.percentage) AS averageScore
        FROM result r
        INNER JOIN question_groups g ON r.groupId = g.id
        WHERE r.userId = :userId
        GROUP BY r.groupId, g.name
    """)
    fun getAllResults(userId: String): Flow<List<GroupSummary>> // no suspend - flow

    @Query("SELECT * FROM result WHERE groupId = :groupId AND userId = :userId")
    fun getAllForGroup(groupId: String, userId: String): Flow<List<ResultEntity>>

    @Query("DELETE FROM result WHERE groupId = :groupId AND userId = :userId")
    suspend fun deleteAllForGroup(groupId: String, userId: String)

    @Transaction
    suspend fun refreshResult(groupId: String, userId: String, result: List<ResultEntity>) {
        deleteAllForGroup(groupId, userId)
        insertAll(result)
    }
}

