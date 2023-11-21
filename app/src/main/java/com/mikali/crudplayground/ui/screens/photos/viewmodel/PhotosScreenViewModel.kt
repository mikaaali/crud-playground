package com.mikali.crudplayground.ui.screens.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.ui.screens.photos.model.ImageItemResponse
import com.mikali.crudplayground.network.downloadmanager.AppDownloadManager
import com.mikali.crudplayground.ui.screens.photos.repository.PhotoRepository
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

    private val _downloadRequested = MutableStateFlow<Boolean?>(null)
    val downloadRequested: StateFlow<Boolean?> = _downloadRequested

    private val photoRepository = PhotoRepository()

    init {
        fetchAllImages()
    }

    private fun fetchAllImages() {
        viewModelScope.launch {
            val images = photoRepository.getAllPhotos()
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
                _downloadRequested.value = true
            } catch (e: Exception) {
                _downloadRequested.value = false
                println("Can't download file")
            }
        }
    }

    sealed class PhotosScreenEvent {
        object DownloadSuccess : PhotosScreenEvent()
        object DownloadFailure : PhotosScreenEvent()
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