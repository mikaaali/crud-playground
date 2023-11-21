package com.mikali.crudplayground.downloadmanager

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DownloadCompletedReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra("extra_download_id", -1)
            if (id != -1L) {
                Toast.makeText(context, "Download Completed with id: $id", Toast.LENGTH_LONG).show()
            }
        }
    }
}
