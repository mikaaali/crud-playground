package com.mikali.crudplayground.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.data.network.model.ImageItem
import com.mikali.crudplayground.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotosScreenViewModel : ViewModel() {

    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    val images: StateFlow<List<ImageItem>> = _images

    private val imageRepository = ImageRepository()

    init {
        fetchAllImages()
    }

    private fun fetchAllImages() {
        viewModelScope.launch {
            val images = imageRepository.getAllImages()
            _images.value = images
        }
    }


}