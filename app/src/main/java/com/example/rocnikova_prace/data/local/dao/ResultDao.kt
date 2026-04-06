package com.example.rocnikova_prace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.rocnikova_prace.data.local.entities.ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: ResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(result: List<ResultEntity>)

    @Query("""
    SELECT 
        id,
        groupId,
        userId,
        percentage
    FROM result
    WHERE userId = :userId
    ORDER BY id DESC
""")
    fun getResultsForUser(userId: String): Flow<List<ResultEntity>>

    @Query("SELECT * FROM result WHERE groupId = :groupId AND userId = :userId")
    fun getAllForGroup(groupId: String, userId: String): Flow<List<ResultEntity>>

    @Query("DELETE FROM result WHERE groupId = :groupId AND userId = :userId")
    suspend fun deleteAllForGroup(groupId: String, userId: String)

    @Query("DELETE FROM result WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: String)

    @Transaction
    suspend fun refreshResult(groupId: String, userId: String, result: List<ResultEntity>) {
        deleteAllForGroup(groupId, userId)
        insertAll(result)
    }
}

