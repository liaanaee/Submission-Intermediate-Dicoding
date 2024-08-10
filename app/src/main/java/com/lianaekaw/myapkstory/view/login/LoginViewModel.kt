package com.lianaekaw.myapkstory.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lianaekaw.myapkstory.pref.UserModel
import com.lianaekaw.myapkstory.repository.UserRepository
import com.lianaekaw.myapkstory.response.LoginResponse

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    var resultlogin : MutableLiveData<LoginResponse> = repository.login
    var isLoading: LiveData<Boolean> = repository.isLoading


    fun forlogin(email: String, password: String) {
        return repository.executeLogin(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        repository.saveUserSession(user)
    }
}