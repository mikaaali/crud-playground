package com.mikali.crudplayground.network.downloadmanager

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

        val request = DownloadManager
            // convert the provided URL string to a Uri object and create a new DownloadManager Request with it
            .Request(url.toUri())
            // Set type of file that can be downloaded, "*/*" indicates any file type, "image/jpeg" tells download manager expected file is type JPEG
            .setMimeType("*/*")
            // below allows downloading over both mobile and Wi-Fi networks
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            // download notification is visible in the system's notification area
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            // set the title of this download, to be displayed in notifications (if UI notification enabled)
            .setTitle(fileName)
            // set a description of this download, to be displayed in notifications (if UI notification enabled)
            .setDescription("Downloaded by CRUDPlayground App")
            // set the OS device destination for the downloaded file to be stored, below it's set to the download folder of the OS
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$time-$fileName")

        // Enqueue the download request in the DownloadManager. 'enqueue' method schedules the download for execution and returns a unique ID for the download.
        return downloadManager.enqueue(request)
    }
}