package com.mikali.crudplayground.ui.screens.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.repository.PostRepository
import kotlinx.coroutines.CancellationException
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

    // TODO- this is StateFlow is ok because we listen for the data in the CreateAndEdit UI and pass it to the CreateAndEdit viewModel,
    //  maybe in the future, a shared view model with child viewModels is better?
    private val _selectedPostItem = MutableStateFlow(PostItem.Empty)
    val selectedPostItem: StateFlow<PostItem> = _selectedPostItem

    private val _eventFlow = MutableSharedFlow<PostListEvent>()
    val eventFlow: Flow<PostListEvent> = _eventFlow.asSharedFlow()

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
                    _eventFlow.emit(PostListEvent.OnFetchAllPostsFail)
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
                            _eventFlow.emit(PostListEvent.OnDeletePostSuccess)
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

    fun onPostSuccessfullyCreated() {
        viewModelScope.launch {
            try {
                Log.d(
                    "haha PostListViewModel",
                    "onPostSuccessfullyCreated(): Starting event emission..."
                )
                _eventFlow.emit(PostListEvent.OnCreatePostSuccess)
                Log.d(
                    "haha PostListViewModel",
                    "onPostSuccessfullyCreated(): Event emitted successfully!"
                )
            } catch (e: CancellationException) {
                Log.e(
                    "haha PostListViewModel",
                    "onPostSuccessfullyCreated(): Coroutine cancelled during emission!",
                    e
                )
            } catch (e: Exception) {
                Log.e(
                    "haha PostListViewModel",
                    "onPostSuccessfullyCreated(): Unexpected error during emission!",
                    e
                )
            }
        }
    }

    sealed class PostListEvent {
        object OnCreatePostSuccess : PostListEvent()
        object OnDeletePostSuccess : PostListEvent()
        object OnFetchAllPostsFail : PostListEvent()
    }
}