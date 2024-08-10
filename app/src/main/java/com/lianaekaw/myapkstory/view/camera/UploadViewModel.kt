package com.lianaekaw.myapkstory.view.camera

import androidx.lifecycle.ViewModel
import com.lianaekaw.myapkstory.repository.UserRepository
import java.io.File

class UploadViewModel (private val repository: UserRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}