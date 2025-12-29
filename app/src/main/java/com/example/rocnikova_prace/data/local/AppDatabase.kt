package com.example.rocnikova_prace.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rocnikova_prace.data.local.dao.GroupDao
import com.example.rocnikova_prace.data.local.dao.QuestionDao
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.local.entities.QuestionEntity

@Database(
    entities = [QuestionEntity::class, GroupEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun groupDao(): GroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration(false)
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}