package com.mikali.crudplayground.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.data.network.model.PostItem
import com.mikali.crudplayground.repository.PostRepository
import com.mikali.crudplayground.ui.model.PostInput
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
    private val _singlePostUiState = MutableStateFlow(PostItem())
    val singlePostUiState: StateFlow<PostItem> = _singlePostUiState

    fun updateTitle(title: String) {
        _singlePostUiState.value = _singlePostUiState.value.copy(title = title)
    }

    fun updateBody(body: String) {
        _singlePostUiState.value = _singlePostUiState.value.copy(body = body)
    }

    fun createNewPost() {
        viewModelScope.launch {
            postRepository.createPost(_singlePostUiState.value.title, _singlePostUiState.value.body)
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
            val posts = postRepository.getAllPosts()
            _postListUiState.value = posts
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
            val postItem = postRepository.getSinglePost(id = id)
            if (postItem != null) {
                _singlePostUiState.value =
                    _singlePostUiState.value.copy(title = postItem.title, body = postItem.body)
            }
        }

    }

    private fun updateExistingPost(id: Int, postInput: PostInput) {
        viewModelScope.launch {
            postRepository.updatePost(id = id, postInput = postInput)
        }
    }

    fun onPostButtonClick(postItem: PostItem) {
        if (postItem.id != null) {
            updateExistingPost(id = postItem.id, postInput = PostInput(title = postItem.title, body = postItem.body))
        } else {
            createNewPost()
        }
    }

    fun onEditButtonClick() {
        _singlePostUiState.value.id?.let {
            getSinglePost(id = it)
        }
    }

    fun onCardClick(id: Int) {
        _singlePostUiState.value = _singlePostUiState.value.copy(
            id = id,
            title = "",
            body = ""
        )
    }

    fun onDeleteButtonClick() {
        viewModelScope.launch {
            _singlePostUiState.value.id?.let {
                postRepository.deleteSinglePost(id = it)
            }
        }
    }

}
