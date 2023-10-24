package com.mikali.crudplayground.service

import androidx.annotation.StringRes

sealed class NetworkResult {

    data class NetworkSuccess<T>(val data: T) : NetworkResult()

    // get message with string resource
    data class NetworkFailure(@StringRes val message: Int) : NetworkResult()
}
