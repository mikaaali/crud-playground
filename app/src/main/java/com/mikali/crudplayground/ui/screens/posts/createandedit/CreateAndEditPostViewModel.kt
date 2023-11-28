package com.mikali.crudplayground.ui.screens.posts.createandedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.repository.PostRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CreateAndEditPostViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {

    private val _postItemUiState = MutableStateFlow(PostItem.Empty)
    val postItemUiState: StateFlow<PostItem> = _postItemUiState

    private val _event = MutableSharedFlow<CreateAndEditPostEvent>()
    val event = _event.asSharedFlow()

    fun updateTitle(title: String) {
        _postItemUiState.value = _postItemUiState.value.copy(title = title)
    }

    fun updateBody(body: String) {
        _postItemUiState.value = _postItemUiState.value.copy(body = body)
    }

    fun clearSelectedPostItem() {
        _postItemUiState.value = PostItem.Empty
    }

    fun setSelectedPostItem(postItem: PostItem) {
        _postItemUiState.value = postItem
    }

    fun updatePost() {
        _postItemUiState.value.id?.let {
            updateExistingPost(
                id = it, postItem = PostItem(
                    title = _postItemUiState.value.title,
                    body = _postItemUiState.value.body
                )
            )
        }
    }

    fun createNewPost() {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.createPost(
                _postItemUiState.value.title, _postItemUiState.value.body
            )

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    _event.emit(CreateAndEditPostEvent.OnCreatePostSuccessful)
                }

                is NetworkResult.NetworkFailure -> {
                    // no-op
                }
            }
        }
    }

    private fun updateExistingPost(id: Int, postItem: PostItem) {
        viewModelScope.launch {
            val networkResult: NetworkResult =
                postRepository.updatePost(id = id, postItem = postItem)

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    _event.emit(CreateAndEditPostEvent.OnUpdatePostSuccessful)
                }

                is NetworkResult.NetworkFailure -> {
                    // no-op
                }
            }
        }
    }

    sealed class CreateAndEditPostEvent {
        object OnCreatePostSuccessful : CreateAndEditPostEvent()
        object OnUpdatePostSuccessful : CreateAndEditPostEvent()
    }
}