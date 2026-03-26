package com.example.rocnikova_prace.data.repository

import android.util.Log
import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.model.Profile
import com.example.rocnikova_prace.data.remote.dto.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


class AuthRepository(
    private val supabase: SupabaseClient,
    private val database: AppDatabase
) {
    suspend fun getRemoteProfile(userId: String): Profile {
        return withContext(Dispatchers.IO) {
            try {
                val response = supabase.from("profiles")
                    .select {
                        filter { eq("user_id", userId) }
                    }
                    .decodeSingle<ProfileDto>()

                Profile(response.name, response.email ?: "", response.avatarUrl)
            } catch (e: Exception) {
                Log.e("AuthRepo", "Chyba načítání profilu: ${e.message}")
                e.printStackTrace()

                Profile("Neznámý uživatel", "", null)
            }
        }
    }

    suspend fun signUp(userEmail: String, userPassword: String, name: String) {
        supabase.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("name", name)
            }
        }
    }

    suspend fun signOut() {
        supabase.auth.signOut()
        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }
    }

    suspend fun uploadAvatar(imageBytes: ByteArray): String {
        val userId = getCurrentUserId() ?: throw Exception("Uživatel není přihlášen")

        val filePath = "$userId/avatar.jpg"
        val bucket = supabase.storage["avatars"]

        bucket.upload(filePath, imageBytes) {
            upsert = true
        }

        val publicUrl = bucket.publicUrl(filePath)

        supabase.auth.updateUser {
            data = buildJsonObject {
                put("avatar_url", publicUrl)
            }
        }

        return publicUrl
    }

    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}