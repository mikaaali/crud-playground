package com.mikali.crudplayground.ui.screens.posts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {
    private val _postListUiState = MutableStateFlow<List<PostItem>>(emptyList())
    val postListUiState: StateFlow<List<PostItem>> = _postListUiState

    // TODO- Thursday: use private lateinit var
    private val _selectedPostItem = MutableStateFlow(PostItem.Empty)
    val selectedPostItem: StateFlow<PostItem> = _selectedPostItem

    private val _events = MutableSharedFlow<PostListEvent>()
    val events: Flow<PostListEvent> = _events.asSharedFlow()

    init {
        fetchAllPosts()
    }

    fun fetchAllPosts() {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.getAllPosts()

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    val posts: List<PostItem> = networkResult.data as List<PostItem>
                    _postListUiState.value = posts
                }

                is NetworkResult.NetworkFailure -> {
                    // TODO- Thursday: add event to show UI error view
                }
            }
        }
    }

    fun setSelectedPostItem(postItem: PostItem) {
        _selectedPostItem.value = postItem
    }

    fun onDeleteButtonClick() {
        val selectedDeleteId = selectedPostItem.value.id
        if (selectedDeleteId != null) {
            viewModelScope.launch {
                try {
                    val networkResult: NetworkResult =
                        postRepository.deleteSinglePost(id = selectedDeleteId)
                    when (networkResult) {
                        is NetworkResult.NetworkSuccess<*> -> {
                            _events.emit(PostListEvent.OnSuccessDeletePost)
                        }

                        is NetworkResult.NetworkFailure -> {
                            // no-op
                        }
                    }
                } finally {
                    // Reset selected item, even if deletion fails
                    _selectedPostItem.value = PostItem.Empty
                }
            }
        }
    }

    sealed class PostListEvent {
        object OnSuccessDeletePost : PostListEvent()
    }
}