package com.mikali.crudplayground.ui.post.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.navigation.EditMode
import com.mikali.crudplayground.repository.PostRepository
import com.mikali.crudplayground.service.NetworkResult
import com.mikali.crudplayground.ui.post.PostCreationEvent
import com.mikali.crudplayground.ui.post.model.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * We use to have a EditScreenViewModel and a ListScreenViewModel, because we want
 * each UI Screen to have it's own viewModel, but since listScreen and editScreen
 * manipulate the same data, it makes sense for them to share 1 viewModel
 */
class PostSharedViewModel : ViewModel() {

    private val postRepository: PostRepository = PostRepository()

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

    private val _singlePostCreationEvent = MutableStateFlow(PostCreationEvent.IDLE)
    val singlePostCreationEvent: StateFlow<PostCreationEvent> = _singlePostCreationEvent

    fun resetNetworkStatus() {
        _singlePostCreationEvent.value = PostCreationEvent.IDLE
    }

    fun updateTitle(title: String) {
        _singlePostUiState.value = _singlePostUiState.value.copy(title = title)
    }

    fun updateBody(body: String) {
        _singlePostUiState.value = _singlePostUiState.value.copy(body = body)
    }

    fun createNewPost() {
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
                    _singlePostCreationEvent.value = PostCreationEvent.SUCCESS
                    //clear edit screen
                    _singlePostUiState.value = PostItem(
                        id = null,
                        title = null,
                        body = null,
                    )
                }

                is NetworkResult.NetworkFailure -> {
                    //TODO, NetworkRequestStatus should be a sealed class, so error can contain message
                    _singlePostCreationEvent.value = PostCreationEvent.ERROR
                    Log.d("haha", "${networkResult.message}")
                }
            }
        }
    }

    fun clearSinglePostUiState() {
        _singlePostUiState.value = PostItem.Empty
    }
    //endregion

    // region State for ListScreen
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

    private fun getSinglePost(id: Int) {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.getSinglePost(id = id)

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    val postItem = networkResult.data as PostItem
                    _singlePostUiState.value =
                        _singlePostUiState.value.copy(
                            title = postItem.title,
                            body = postItem.body
                        )
                }

                is NetworkResult.NetworkFailure -> {
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
                    _singlePostCreationEvent.value = PostCreationEvent.SUCCESS
                    // Update the edit screen to show the updated post
                    _singlePostUiState.value = updatedPost
                }

                is NetworkResult.NetworkFailure -> {
                    _singlePostCreationEvent.value = PostCreationEvent.ERROR
                    Log.e("ViewModel", "Error updating post: ${networkResult.message}")
                }
            }
        }
    }

    fun onPostButtonClick(editMode: EditMode, postItem: PostItem) {
        when (editMode) {
            EditMode.CREATE -> {
                createNewPost()
            }

            EditMode.EDIT -> {
                postItem.id?.let {
                    updateExistingPost(
                        id = it,
                        postItem = PostItem(title = postItem.title, body = postItem.body)
                    )
                }
            }
        }

    }

    fun onEditButtonClick(id: Int?) {
        id?.let {
            getSinglePost(id = it)
        }
    }

    fun setCurrentSelectSinglePostItem(postItem: PostItem) {
        _singlePostUiState.value = postItem
    }

    fun onDeleteButtonClick(id: Int) {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.deleteSinglePost(id = id)

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    println("hahaha _postListUiState: ${_postListUiState.value}")
                    println("hahahaid: $id")
                    // Remove the post with the matching id from the list
                    val newList: List<PostItem> = _postListUiState.value.filterNot { it.id == id }
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


    //Asynchronous execution using enqueue and callbacks.
    /*    private fun fetchAllPosts() {
            viewModelScope.launch {
                postRepository.getAllPosts() { fetchedPosts, err ->
                    if (fetchedPosts != null) {
                        */
    /**
     * Using a sealed class can be a more idiomatic way to represent either
     * a successful result or an error, instead of two separate instances of StateFlow
     *//*
                    _posts.value = fetchedPosts
                } else if (err != null) {
                    _error.value = err
                }
            }
        }
    }*/
}
