package com.lianaekaw.myapkstory.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[token] = user.token
            preferences[name] = user.name
            preferences[userId] = user.userId
            preferences[isLogin] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[name].toString(),
                preferences[userId].toString(),
                preferences[token].toString(),
                preferences[isLogin] ?: false
            )
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[token] ?: ""
        }
    }
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var instance: UserPreference? = null
        private val name = stringPreferencesKey("name")
        private val userId = stringPreferencesKey("userId")
        private val token = stringPreferencesKey("token")
        private val isLogin = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return instance ?: synchronized(this) {
                val newInstance = UserPreference(dataStore)
                instance = newInstance
                newInstance
            }
        }
    }
}