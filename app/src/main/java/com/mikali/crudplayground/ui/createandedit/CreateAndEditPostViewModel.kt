package com.mikali.crudplayground.ui.createandedit

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.repository.PostRepository
import com.mikali.crudplayground.service.NetworkResult
import com.mikali.crudplayground.ui.posts.enums.EditMode
import com.mikali.crudplayground.ui.posts.model.PostItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class CreateAndEditPostViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {

    private val _postUiState = MutableStateFlow(PostItem.Empty)
    val postUiState: StateFlow<PostItem> = _postUiState

    private val _event = MutableSharedFlow<CreateAndEditPostEvent>()
    val event = _event.distinctUntilChanged()

    fun updateTitle(title: String) {
        _postUiState.value = _postUiState.value.copy(title = title)
    }

    fun updateBody(body: String) {
        _postUiState.value = _postUiState.value.copy(body = body)
    }

    fun clearSinglePostUiState() {
        _postUiState.value = PostItem.Empty
    }

    fun onPostButtonClick(editMode: EditMode) {
        when (editMode) {
            EditMode.CREATE -> {
                createNewPost()
            }

            EditMode.EDIT -> {

                // Use MutableStateFlow id instead of Flow id from UI because this is a single/ one time event when user clicks the button
                _postUiState.value.id?.let {
                    updateExistingPost(
                        id = it, postItem = PostItem(
                            title = _postUiState.value.title,
                            body = _postUiState.value.body
                        )
                    )
                }

            }
        }
    }

    private fun createNewPost() {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.createPost(
                _postUiState.value.title, _postUiState.value.body
            )

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    println("chris postCreate successful")
                    _event.emit(CreateAndEditPostEvent.OnCreatePostSuccessFul)
                }

                is NetworkResult.NetworkFailure -> {

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
                }

                is NetworkResult.NetworkFailure -> {

                }
            }
        }
    }

    sealed class CreateAndEditPostEvent {
        object OnCreatePostSuccessFul : CreateAndEditPostEvent()
        object OnUpdatePostSuccessFul : CreateAndEditPostEvent()
    }
}