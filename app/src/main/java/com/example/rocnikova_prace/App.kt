package com.example.rocnikova_prace

import android.app.Application
import androidx.room.Room
import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.repository.QuestionRepository

class App : Application() {

    lateinit var database: AppDatabase
    lateinit var repository: QuestionRepository

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "questions.db"
        ).build()

        repository = QuestionRepository(database.questionDao())
    }
}