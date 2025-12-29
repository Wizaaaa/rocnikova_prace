    package com.example.rocnikova_prace

    import android.app.Application
    import com.example.rocnikova_prace.data.local.AppDatabase
    import com.example.rocnikova_prace.data.repository.QuestionRepository

    class App : Application() {
        lateinit var repository: QuestionRepository

        override fun onCreate() {
            super.onCreate()

            val database = AppDatabase.getDatabase(this)

            repository = QuestionRepository(
                questionDao = database.questionDao(),
                groupDao = database.groupDao()
            )
        }
    }