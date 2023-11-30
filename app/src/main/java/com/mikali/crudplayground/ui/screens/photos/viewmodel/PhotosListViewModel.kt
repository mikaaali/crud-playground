package com.mikali.crudplayground.ui.screens.photos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mikali.crudplayground.network.downloadmanager.AppDownloadManager
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.ui.screens.photos.model.PhotoItem
import com.mikali.crudplayground.ui.screens.photos.repository.PhotoRepository
import com.mikali.crudplayground.util.generateFileName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PhotosListViewModel(
    private val appDownloadManager: AppDownloadManager,
) : ViewModel() {

    // TODO - should in the viewModel constructor
    private val photoRepository = PhotoRepository()

    private val _images = MutableStateFlow<List<PhotoItem>>(emptyList())
    val images: StateFlow<List<PhotoItem>> = _images

    private lateinit var imageUrl: String

    private val _eventFlow = MutableSharedFlow<PhotosListEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        fetchAllImages()
    }

    private fun fetchAllImages() {
        viewModelScope.launch {
            val networkResult: NetworkResult = photoRepository.getAllPhotos()

            when (networkResult) {
                is NetworkResult.NetworkSuccess<*> -> {
                    val images: List<PhotoItem> = networkResult.data as List<PhotoItem>
                    _images.value = images
                }

                is NetworkResult.NetworkFailure -> {
                    _eventFlow.emit(PhotosListEvent.ShowNetworkError)
                }
            }
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

    sealed class PhotosListEvent {
        object ShowNetworkError : PhotosListEvent()
    }


    class PhotosScreenViewModelFactory(
        private val appDownloadManager: AppDownloadManager
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PhotosListViewModel(
                appDownloadManager = appDownloadManager
            ) as T
        }
    }

}