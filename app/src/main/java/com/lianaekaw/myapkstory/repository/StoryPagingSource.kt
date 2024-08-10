package com.lianaekaw.myapkstory.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lianaekaw.myapkstory.data.ApiService
import com.lianaekaw.myapkstory.pref.UserPreference
import com.lianaekaw.myapkstory.response.ListStoryItem

class StoryPagingSource(private val apiService: ApiService, private val userPreference: UserPreference): PagingSource<Int, ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
// https://console.developers.google.com/flows/enableapi?apiid=maps-android-backend.googleapis.com&keyType=CLIENT_SIDE_ANDROID&r=C2:6A:7A:6B:BD:0E:2F:96:B4:01:06:AA:72:61:4F:5A:B7:F7:06:D9%3Bcom.lianaekaw.myapkstory

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStory( position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory.map {
                    ListStoryItem(it.photoUrl, it.createdAt, it.name, it.description, it.lon, it.id, it.lat)
                },
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}