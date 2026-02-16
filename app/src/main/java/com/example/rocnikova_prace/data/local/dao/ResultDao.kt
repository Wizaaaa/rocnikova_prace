package com.example.rocnikova_prace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.rocnikova_prace.data.local.entities.ResultEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ResultDao {
    @Insert
    suspend fun insert(result: ResultEntity)

    @Insert
    suspend fun insertAll(result: List<ResultEntity>)

    @Query("SELECT * FROM result")
    fun getAllResults(): Flow<List<ResultEntity>> // no suspend - flow

    @Query("SELECT * FROM result WHERE groupId = :groupId")
    fun getAllForGroup(groupId: String): Flow<List<ResultEntity>>

    @Query("DELETE FROM result WHERE groupId = :groupId")
    suspend fun deleteAllForGroup(groupId: String)

    @Transaction
    suspend fun refreshResult(groupId: String, result: List<ResultEntity>) {
        deleteAllForGroup(groupId)
        insertAll(result)
    }
}

