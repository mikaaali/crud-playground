package com.mikali.crudplayground.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.model.PostItem
import com.mikali.crudplayground.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val post: StateFlow<List<PostItem>> = _posts

    private val postRepository = PostRepository()

    init {
        fetchAllPosts()
    }

    private fun fetchAllPosts() {
        viewModelScope.launch {
            val response = postRepository.getAllPosts()
            _posts.value = response
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