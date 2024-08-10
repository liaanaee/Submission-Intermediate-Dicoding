package com.lianaekaw.myapkstory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lianaekaw.myapkstory.di.Injection
import com.lianaekaw.myapkstory.repository.UserRepository
import com.lianaekaw.myapkstory.view.camera.UploadViewModel
import com.lianaekaw.myapkstory.view.login.LoginViewModel
import com.lianaekaw.myapkstory.view.main.MainViewModel
import com.lianaekaw.myapkstory.view.maps.MapsViewModel
import com.lianaekaw.myapkstory.view.signup.SignupViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun clearInstance() {
            UserRepository.clearInstance()
            INSTANCE = null
        }

        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}