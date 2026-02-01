package com.example.rocnikova_prace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rocnikova_prace.data.local.entities.ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {
    @Insert
    suspend fun insert(result: ResultEntity)

    @Query("SELECT * FROM result WHERE groupId = :groupId")
    fun getAllForGroup(groupId: String): Flow<List<ResultEntity>>

    @Query("DELETE FROM result WHERE groupId = :groupId")
    suspend fun deleteAllForGroup(groupId: String)
}