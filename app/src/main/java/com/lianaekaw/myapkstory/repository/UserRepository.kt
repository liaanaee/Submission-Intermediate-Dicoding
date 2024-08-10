package com.lianaekaw.myapkstory.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.lianaekaw.myapkstory.data.ApiService
import com.lianaekaw.myapkstory.pref.UserModel
import com.lianaekaw.myapkstory.pref.UserPreference
import com.lianaekaw.myapkstory.response.ErrorResponse
import com.lianaekaw.myapkstory.response.ListStoryItem
import com.lianaekaw.myapkstory.response.LoginResponse
import com.lianaekaw.myapkstory.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    private var loginResult = MutableLiveData<LoginResponse>()
    var login: MutableLiveData<LoginResponse> = loginResult

    var thisLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = thisLoading

    private var Alllist = MutableLiveData<List<ListStoryItem>>()
    var lists: MutableLiveData<List<ListStoryItem>> = Alllist

    fun seeStories() {
        thisLoading.value = true
        val client = apiService.getStories()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    thisLoading.value = false
                    Alllist.value = response.body()?.listStory
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                thisLoading.value = false
                Log.e("Repository", "error: ${t.message}")
            }
        })
    }

    fun getTheStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, userPreference)
            }
        ).liveData
    }

    fun executeLogin(email: String, password: String) {
        thisLoading.value = true
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    thisLoading.value = false
                    loginResult.value = response.body()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Repository", "error: ${t.message}")
            }
        })
    }

    fun getStoriesLocation(): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.getStoriesWithLocation(location = 1)
            val result = response.listStory
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    suspend fun register(name: String, email: String, password: String): ErrorResponse {
        return apiService.register(name, email, password)
    }

    fun uploadImage(imageFile: File, description: String): LiveData<ResultState<ErrorResponse>> = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveUserSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getUserSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun clearUserSession() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun clearInstance() {
            instance = null
        }

        fun getInstance(apiService: ApiService, userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}