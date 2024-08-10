package com.lianaekaw.myapkstory.di

import android.content.Context
import com.lianaekaw.myapkstory.data.ApiConfig
import com.lianaekaw.myapkstory.pref.UserPreference
import com.lianaekaw.myapkstory.pref.dataStore
import com.lianaekaw.myapkstory.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}