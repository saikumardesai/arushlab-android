package com.arushlab.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Note: Provide valid Supabase URL and Key securely in production (e.g., via BuildConfig)
    private val SUPABASE_URL = com.arushlab.android.BuildConfig.SUPABASE_URL
    private val SUPABASE_KEY = com.arushlab.android.BuildConfig.SUPABASE_KEY

import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

    @Provides
    @Singleton
    fun providePostgrest(client: SupabaseClient): Postgrest {
        return client.postgrest
    }
}
