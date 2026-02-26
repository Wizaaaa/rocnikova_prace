package com.example.rocnikova_prace.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://ueqiqucisvmqafwllasi.supabase.co",
        supabaseKey = "sb_publishable_zo9KU9n0pOFOEnZi-6OC1Q_3na-nFFS"
    ) {
        install(Postgrest) {
            serializer = KotlinXSerializer()
        }
        install(Auth) {

        }
        install(ComposeAuth) {
            googleNativeLogin(serverClientId = "356671901841-1cb1pta371p8ng8iae4uujg8eduu2pmd.apps.googleusercontent.com")
        }
    }
}