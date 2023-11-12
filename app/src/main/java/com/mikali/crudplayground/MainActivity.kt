package com.mikali.crudplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.ui.main.MainScreen
import com.mikali.crudplayground.ui.theme.CRUDPlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDPlaygroundTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }

    }
}


