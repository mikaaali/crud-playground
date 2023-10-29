package com.mikali.crudplayground.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.repository.PostRepository
import com.mikali.crudplayground.service.NetworkResult
import com.mikali.crudplayground.ui.model.PostItem
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

    private val _singlePostNetworkRequestStatus = MutableStateFlow(NetworkRequestStatus.IDLE)
    val singlePostNetworkRequestStatus: StateFlow<NetworkRequestStatus> =
        _singlePostNetworkRequestStatus

    fun resetNetworkStatus() {
        _singlePostNetworkRequestStatus.value = NetworkRequestStatus.IDLE
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
                    _singlePostNetworkRequestStatus.value = NetworkRequestStatus.SUCCESS
                    //Add this new value to the listScreen
                    //_postListUiState.value =
                }

                is NetworkResult.NetworkFailure -> {
                    _singlePostNetworkRequestStatus.value = NetworkRequestStatus.ERROR
                    Log.d("haha", "${networkResult.message}")
                }
            }
        }
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
                    val posts: List<PostItem> = networkResult.data as List<PostItem>
                    _postListUiState.value = posts
                }

                is NetworkResult.NetworkFailure -> {
                    Log.d("haha", "${networkResult.message}")
                    //TODO - emit error UI state, listen for the error UI state in the UI do show a error view
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
            postRepository.updatePost(id = id, postItem = postItem)
        }
    }

    fun onPostButtonClick(postItem: PostItem) {
        if (postItem.id != null) {
            updateExistingPost(
                id = postItem.id,
                postItem = PostItem(title = postItem.title, body = postItem.body)
            )
        } else {
            createNewPost()
        }
    }

    fun onEditButtonClick(id: Int?) {
        id?.let {
            getSinglePost(id = it)
        }
    }

    fun onCardClick(postItem: PostItem) {
        _singlePostUiState.value = postItem
    }

    fun onDeleteButtonClick(id: Int) {
        viewModelScope.launch {
            val networkResult: NetworkResult = postRepository.deleteSinglePost(id = id)

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    // TODO - show user you have successfully deleted an item UI
                }

                is NetworkResult.NetworkFailure -> {
                    // TODO- show user you cannot delete an item
                }
            }
        }
    }

}
