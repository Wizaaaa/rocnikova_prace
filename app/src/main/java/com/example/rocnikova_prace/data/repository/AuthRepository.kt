package com.example.rocnikova_prace.data.repository

import com.example.rocnikova_prace.data.local.AppDatabase
import com.example.rocnikova_prace.data.model.Profile
import com.example.rocnikova_prace.data.remote.dto.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
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

                Profile(
                    id = response.userId,
                    userName = response.name,
                    email = response.email,
                    avatarUrl = response.avatarUrl,
                    notificationsEnabled = response.notificationsEnabled
                )
            } catch (_: Exception) {
                Profile("", "Neznámý uživatel", "", null, true)
            }
        }
    }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            supabase.auth.signOut()
            database.clearAllTables()
        }
    }

    suspend fun uploadAvatar(imageBytes: ByteArray): String {
        return withContext(Dispatchers.IO) {
            try {
                val userId = getCurrentUserId()
                    ?: throw Exception("Uživatel není přihlášen")

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

                publicUrl
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun updateFcmToken(userId: String, token: String) {
        withContext(Dispatchers.IO) {
            try {
                supabase.from("profiles").update(
                    {
                        set("fcm_token", token)
                    }
                ) {
                    filter {
                        eq("user_id", userId)
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun toggleNotifications(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = supabase.from("profiles")
                    .select {
                        filter { eq("user_id", userId) }
                    }
                    .decodeSingle<ProfileDto>()

                val newStatus = !response.notificationsEnabled

                supabase.from("profiles").update(
                    buildJsonObject {
                        put("notifications_enabled", newStatus)
                    }
                ) {
                    filter { eq("user_id", userId) }
                }

                newStatus
            } catch (_: Exception) {
                false
            }
        }
    }

    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}