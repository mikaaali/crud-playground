package com.mikali.crudplayground

import android.app.DownloadManager
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.network.downloadmanager.DownloadCompletedReceiver
import com.mikali.crudplayground.ui.main.MainScreen
import com.mikali.crudplayground.ui.theme.CRUDPlaygroundTheme

class MainActivity : ComponentActivity() {

    private lateinit var downloadCompletedReceiver: DownloadCompletedReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDPlaygroundTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // registering a BroadcastReceiver handled at the activity level
        downloadCompletedReceiver = DownloadCompletedReceiver()
        //DownloadManager.ACTION_DOWNLOAD_COMPLETE is broadcasted by the OS's DownloadManager when a download completes
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        // tell broadcast receiver to listen for download complete action
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(downloadCompletedReceiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(downloadCompletedReceiver, filter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(downloadCompletedReceiver)
    }
}


