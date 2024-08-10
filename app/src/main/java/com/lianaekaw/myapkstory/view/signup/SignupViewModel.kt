package com.lianaekaw.myapkstory.view.signup

import androidx.lifecycle.ViewModel
import com.lianaekaw.myapkstory.repository.UserRepository
import com.lianaekaw.myapkstory.response.ErrorResponse

class SignupViewModel (private var repository: UserRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): ErrorResponse {
        return repository.register(name, email, password)
    }
}