package com.lianaekaw.myapkstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lianaekaw.myapkstory.pref.UserModel
import com.lianaekaw.myapkstory.repository.UserRepository
import com.lianaekaw.myapkstory.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel(){

    private val logoutSuccess = MutableLiveData<Boolean>()
    val logoutCompleted: LiveData<Boolean>
        get() = logoutSuccess

    var itsloading: LiveData<Boolean> = repository.thisLoading

    fun seeStories(): LiveData<List<ListStoryItem>?> {
        repository.seeStories()
        return repository.lists
    }

    fun getUserSession(): LiveData<UserModel> {
        return repository.getUserSession().asLiveData()
    }

    private val refresh = MutableLiveData<Unit>()
    init {
        refreshData()
    }

    private fun refreshData() {
        refresh.value = Unit
    }

    val story: LiveData<PagingData<ListStoryItem>> = refresh.switchMap {
       repository.getTheStory().cachedIn(viewModelScope)
    }

    fun clearUser() {
        viewModelScope.launch {
            repository.clearUserSession()
            logoutSuccess.postValue(true)
        }
    }

}