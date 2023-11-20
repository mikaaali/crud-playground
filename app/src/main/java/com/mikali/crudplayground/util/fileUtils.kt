package com.mikali.crudplayground.util

import java.net.URL
import java.util.Date

fun generateFileName(imageUrl: String): String {

    val originalFileName: String = extractFileNameFromUrl(urlString = imageUrl)

    val time: String = Date().time.toString() // get a Unix timestamp in milliseconds
    return "$time-$originalFileName"
}

fun extractFileNameFromUrl(urlString: String): String {
    val url = URL(urlString)
    // file name is after the last '/'
    return url.path.substringAfterLast(delimiter = '/')
}