package com.mikali.crudplayground.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikali.crudplayground.ui.theme.sandYellow
import com.mikali.crudplayground.ui.theme.tealGreen

@Composable
fun MainScreenTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(tealGreen)
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Today ", color = sandYellow, fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Mo - Jan 30",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}