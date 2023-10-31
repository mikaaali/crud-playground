package com.mikali.crudplayground.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.model.PostItem
import com.mikali.crudplayground.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel : ViewModel() {

    private val _myPost = MutableStateFlow<List<PostItem>>(emptyList())
    val myPost: StateFlow<List<PostItem>> = _myPost

    private val postRepository = PostRepository()

    init {
        fetchAllPosts()
    }


    private fun fetchAllPosts() {
        viewModelScope.launch {
            val response = postRepository.getAllPosts()
            _myPost.value = response
        }
    }

}