package com.mikali.crudplayground.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import java.util.Date

class AppDownloadManagerImpl(context: Context) : AppDownloadManager {


    private val downloadManager: DownloadManager =
        context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String, fileName: String): Long {
        val time: String = Date().time.toString()

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("*/*")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDescription("Downloading")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$time-$fileName")
        return downloadManager.enqueue(request)

    }
}