package com.mikali.crudplayground.ui.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.repository.PostRepository
import com.mikali.crudplayground.service.NetworkResult
import com.mikali.crudplayground.ui.posts.model.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postRepository: PostRepository = PostRepository()
): ViewModel() {
    private val _postListUiState = MutableStateFlow<List<PostItem>>(emptyList())
    val postListUiState: StateFlow<List<PostItem>> = _postListUiState

    init {
        fetchAllPosts()
    }

    fun fetchAllPosts() {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.getAllPosts()

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    val posts: List<PostItem> = networkResult.data as List<PostItem> //TODO-unsafe cast, make NetworkResult generic<T>
                    _postListUiState.value = posts
                }

                is NetworkResult.NetworkFailure -> {
                    Log.d("haha", "${networkResult.message}")
                    //TODO - emit error UI state, listen for the error UI state in the UI do show a error view
                }
            }
        }
    }
}