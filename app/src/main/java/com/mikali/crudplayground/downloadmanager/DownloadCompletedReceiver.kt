package com.mikali.crudplayground.downloadmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadCompletedReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra("extra_download_id", -1)
            if(id != -1L) {
                println("Download Completed with id: $id")
            }
        }

    }


}
