package com.mikali.crudplayground.ui.screens.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.ui.screens.posts.repository.PostRepository
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postRepository: PostRepository = PostRepository()
): ViewModel() {
    private val _postListUiState = MutableStateFlow<List<PostItem>>(emptyList())
    val postListUiState: StateFlow<List<PostItem>> = _postListUiState

    private var selectedPostItem: PostItem? = null

    private val _events = MutableSharedFlow<PostListEvent>()
    val events: Flow<PostListEvent> = _events.asSharedFlow()

    init {
        fetchAllPosts()
    }

    fun fetchAllPosts() {
        viewModelScope.launch {
            println("chris fetchAllPosts")
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

    fun setSelectedPostItem(postItem: PostItem) {
        selectedPostItem = postItem
    }

    fun getSelectedPostItem(): PostItem? = selectedPostItem

    fun onDeleteButtonClick() {
        val selectedDeleteId = selectedPostItem?.id
        if (selectedDeleteId != null) {
            viewModelScope.launch {
                try {
                    val networkResult: NetworkResult = postRepository.deleteSinglePost(id = selectedDeleteId)
                    when (networkResult) {
                        is NetworkResult.NetworkSuccess<*> -> {
                            _events.emit(PostListEvent.OnSuccessDeletePost)
                        }
                        is NetworkResult.NetworkFailure -> {
                            // TODO - Handle failure UI state
                        }
                    }
                } finally {
                    // Reset selected item, even if deletion fails
                    selectedPostItem = null
                }
            }
        }
    }

    sealed class PostListEvent {
        object OnSuccessDeletePost: PostListEvent()
    }
}