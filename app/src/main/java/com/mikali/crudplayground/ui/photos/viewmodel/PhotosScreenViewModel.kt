package com.mikali.crudplayground.ui.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.data.network.model.ImageItemResponse
import com.mikali.crudplayground.downloadmanager.AppDownloadManager
import com.mikali.crudplayground.repository.ImageRepository
import com.mikali.crudplayground.util.generateFileName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhotosScreenViewModel(
    private val appDownloadManager: AppDownloadManager,
) : ViewModel() {

    private val _images = MutableStateFlow<List<ImageItemResponse>>(emptyList())
    val images: StateFlow<List<ImageItemResponse>> = _images

    private lateinit var imageUrl: String

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

    fun onPhotoClicked(imageUrl: String) {
        this.imageUrl = imageUrl
    }

    fun onDownloadClick() {
        downloadFile(imageUrl = imageUrl)
    }

    private fun downloadFile(imageUrl: String) {
        viewModelScope.launch {
            try {
                val fileName = generateFileName(imageUrl = imageUrl)
                val downloadId = appDownloadManager.downloadFile(imageUrl, fileName)
            } catch (e: Exception) {
                println("Can't download file")
            }
        }
    }

    class PhotosScreenViewModelFactory(
        private val appDownloadManager: AppDownloadManager
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PhotosScreenViewModel(
                appDownloadManager = appDownloadManager
            ) as T
        }
    }

}