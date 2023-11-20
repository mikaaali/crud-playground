package com.mikali.crudplayground.downloadmanager

interface AppDownloadManager {

    /**
     * Download a file from a given url and save it with the given fileName
     *
     * @param url the url of the file to download
     * @param fileName the name of the file to save
     * @return the id of the download
     */
    fun downloadFile(url: String, fileName: String) : Long
}