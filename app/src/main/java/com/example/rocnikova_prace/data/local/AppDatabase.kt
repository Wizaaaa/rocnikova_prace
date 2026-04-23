package com.example.rocnikova_prace.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rocnikova_prace.data.local.dao.GroupDao
import com.example.rocnikova_prace.data.local.dao.QuestionDao
import com.example.rocnikova_prace.data.local.dao.ResultDao
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.local.entities.QuestionEntity
import com.example.rocnikova_prace.data.local.entities.ResultEntity

@Database(
    entities = [QuestionEntity::class, GroupEntity::class, ResultEntity::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun groupDao(): GroupDao
    abstract fun resultDao(): ResultDao

    companion object {
        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE question_groups ADD COLUMN is_global INTEGER NOT NULL DEFAULT 0")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addMigrations(MIGRATION_6_7)
                .fallbackToDestructiveMigration(false)
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}