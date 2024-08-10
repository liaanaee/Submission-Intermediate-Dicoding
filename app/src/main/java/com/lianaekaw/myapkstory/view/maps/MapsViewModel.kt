package com.lianaekaw.myapkstory.view.maps

import androidx.lifecycle.ViewModel
import com.lianaekaw.myapkstory.repository.UserRepository

class MapsViewModel (private val userRepository: UserRepository) : ViewModel(){

    fun getStoryWithLocation() = userRepository.getStoriesLocation()
}