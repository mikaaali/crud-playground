package com.mikali.crudplayground.ui.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.repository.PostRepository
import com.mikali.crudplayground.service.NetworkResult
import com.mikali.crudplayground.ui.posts.enums.EditMode
import com.mikali.crudplayground.ui.posts.enums.SinglePostNetworkStatus
import com.mikali.crudplayground.ui.posts.model.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * We use to have a EditScreenViewModel and a ListScreenViewModel, because we want
 * each UI Screen to have it's own viewModel, but since listScreen and editScreen
 * manipulate the same data, it makes sense for them to share 1 viewModel
 */
class PostSharedViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {
    // region State for EditScreen
    /**
     * Granularity: Group related states together
     * but avoid making the state so large
     * that it becomes unwieldy or difficult to understand
     */
    private val _singlePostUiState = MutableStateFlow(
        PostItem(
            id = null,
            title = null,
            body = null,
        )
    )
    val singlePostUiState: StateFlow<PostItem> = _singlePostUiState

    private val _singleSinglePostNetworkStatus = MutableStateFlow(SinglePostNetworkStatus.IDLE)
    val singleSinglePostNetworkStatus: StateFlow<SinglePostNetworkStatus> =
        _singleSinglePostNetworkStatus

    fun resetNetworkStatus() {
        _singleSinglePostNetworkStatus.value = SinglePostNetworkStatus.IDLE
    }

    fun updateTitle(title: String) {
        _singlePostUiState.value = _singlePostUiState.value.copy(title = title)
    }

    fun updateBody(body: String) {
        _singlePostUiState.value = _singlePostUiState.value.copy(body = body)
    }

    fun clearSinglePostUiState() {
        _singlePostUiState.value = PostItem.Empty
    }
    //endregion

    // region State for ListScreen
    private val _postListUiState = MutableStateFlow<List<PostItem>>(emptyList())
    val postListUiState: StateFlow<List<PostItem>> = _postListUiState

//    init {
//        fetchAllPosts()
//    }

    fun fetchAllPosts() {
        viewModelScope.launch {

            val networkResult: NetworkResult = postRepository.getAllPosts()

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    val posts: List<PostItem> =
                        networkResult.data as List<PostItem> //TODO-unsafe cast, make NetworkResult generic<T>
                    _postListUiState.value = posts
                }

                is NetworkResult.NetworkFailure -> {
                    Log.d("haha", "${networkResult.message}")
                    //TODO - emit error UI state, listen for the error UI state in the UI do show a error view
                }
            }
        }
    }
    //endregion


    fun onPostButtonClick(editMode: EditMode) {
        when (editMode) {
            EditMode.CREATE -> {
                createNewPost()
            }

            EditMode.EDIT -> {

                // Use MutableStateFlow id instead of Flow id from UI because this is a single/ one time event when user clicks the button
                _singlePostUiState.value.id?.let {
                    updateExistingPost(
                        id = it,
                        postItem = PostItem(
                            title = _singlePostUiState.value.title,
                            body = _singlePostUiState.value.body
                        )
                    )
                }

            }
        }
    }

    private fun createNewPost() {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.createPost(
                _singlePostUiState.value.title,
                _singlePostUiState.value.body
            )

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    val post = networkResult.data as PostItem
                    //Add this new value to the listScreen
                    _postListUiState.value = _postListUiState.value + post
                    _singleSinglePostNetworkStatus.value = SinglePostNetworkStatus.SUCCESS
                    //clear edit screen
                    _singlePostUiState.value = PostItem(
                        id = null,
                        title = null,
                        body = null,
                    )
                }

                is NetworkResult.NetworkFailure -> {
                    //TODO, NetworkRequestStatus should be a sealed class, so error can contain message
                    _singleSinglePostNetworkStatus.value = SinglePostNetworkStatus.ERROR
                    Log.d("haha", "${networkResult.message}")
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
                    // network call returns the updated post object
                    val updatedPost = networkResult.data as PostItem
                    // Replace the existing value in the list
                    _postListUiState.value = _postListUiState.value.map { existingPost ->
                        if (existingPost.id == updatedPost.id) updatedPost else existingPost
                    }
                    _singleSinglePostNetworkStatus.value = SinglePostNetworkStatus.SUCCESS
                    // Update the edit screen to show the updated post
                    _singlePostUiState.value = updatedPost
                }

                is NetworkResult.NetworkFailure -> {
                    _singleSinglePostNetworkStatus.value = SinglePostNetworkStatus.ERROR
                    Log.e("ViewModel", "Error updating post: ${networkResult.message}")
                }
            }
        }
    }

    fun setCurrentSelectSinglePostItem(postItem: PostItem) {
        _singlePostUiState.value = postItem
    }

    fun onDeleteButtonClick() {
        viewModelScope.launch {
            _singlePostUiState.value.id?.let { selectedDeleteId ->
                val networkResult: NetworkResult =
                    postRepository.deleteSinglePost(id = selectedDeleteId)
                when (networkResult) {
                    is NetworkResult.NetworkSuccess<*> -> {
                        // Remove the post with the matching id from the list
                        val newList: List<PostItem> =
                            _postListUiState.value.filterNot { it.id == selectedDeleteId }
                        // Update the UI state with the new list
                        _postListUiState.value = newList
                    }

                    is NetworkResult.NetworkFailure -> {
                        // TODO- show user you cannot delete an item, setup an event first,
                        // then listen for the event in the UI, and show the error UI

                    }
                }
            }

        }
    }
}
