package com.mikali.crudplayground.ui.screens.posts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.ui.screens.posts.repository.PostRepository
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postRepository: PostRepository = PostRepository()
): ViewModel() {
    private val _postListUiState = MutableStateFlow<List<PostItem>>(emptyList())
    val postListUiState: StateFlow<List<PostItem>> = _postListUiState

    private var selectedPostItem: PostItem? = null

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

    fun setSelectedPostItem(postItem: PostItem) {
        selectedPostItem = postItem
    }

    fun getSelectedPostItem(): PostItem? = selectedPostItem

    fun onDeleteButtonClick() {
        viewModelScope.launch {
            println("chris selectedPostId ${selectedPostItem?.id}")
            selectedPostItem?.id?.let { selectedDeleteId ->
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