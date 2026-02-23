package com.example.rocnikova_prace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM question_groups WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllGroups(userId: String): Flow<List<GroupEntity>>

    @Query("SELECT * FROM question_groups WHERE id = :id AND userId = :userId")
    suspend fun getGroupById(id: String, userId: String): GroupEntity?

    @Upsert
    suspend fun insertGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    @Query("DELETE FROM question_groups WHERE userId = :userId")
    suspend fun deleteAll(userId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groups: List<GroupEntity>)

    @Transaction
    suspend fun refreshGroups(userId: String, groups: List<GroupEntity>) {
        deleteAll(userId)
        insertAll(groups)
    }
}