package com.example.rocnikova_prace.data.repository

import com.example.rocnikova_prace.data.local.AppDatabase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


class AuthRepository(
    private val supabase: SupabaseClient,
    private val database: AppDatabase
) {
    suspend fun signUp(userEmail: String, userPassword: String, name: String) {
        supabase.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("name", name)
            }
        }
    }

    suspend fun signIn(userEmail: String, userPassword: String) {
        supabase.auth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
        database.clearAllTables()
    }


    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    suspend fun isNameAvailable(nameToCheck: String): Boolean {
        val nameExists = supabase.postgrest.rpc(
            function = "check_name_exists",
            parameters = mapOf("lookup_name" to nameToCheck)
        ).decodeAs<Boolean>()

        return !nameExists
    }

    suspend fun resetUserPassword(userEmail: String) {
        supabase.auth.resetPasswordForEmail(email = userEmail)
    }
}