package com.example.linkyishop.di

import android.content.Context
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.data.preferences.dataStore
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}