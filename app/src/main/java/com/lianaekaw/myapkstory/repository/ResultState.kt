package com.lianaekaw.myapkstory.repository

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: String) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}