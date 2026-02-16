package com.example.rocnikova_prace

import android.app.Application
import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.data.repository.AuthRepository
import com.example.rocnikova_prace.data.repository.QuestionRepository

class App : Application() {
    lateinit var repository: QuestionRepository
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getDatabase(this)

        repository = QuestionRepository(
            questionDao = database.questionDao(),
            groupDao = database.groupDao(),
            resultDao = database.resultDao()
        )
        authRepository = AuthRepository(
            supabase = SupabaseClient.client,
            database = database
        )
    }
}