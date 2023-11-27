package com.mikali.crudplayground.ui.screens.photos.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikali.crudplayground.ui.screens.photos.viewmodel.PhotosScreenViewModel
import com.mikali.crudplayground.ui.theme.sandYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotosScreenBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    photosScreenViewModel: PhotosScreenViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(20.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // dismiss bottom sheet
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
                photosScreenViewModel.onDownloadClick()
            },
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(width = 2.dp, color = Color.Black),
            colors = ButtonDefaults.buttonColors(containerColor = sandYellow)
        ) {
            Text(
                "Download to device",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Icon(
                imageVector = Icons.Filled.SaveAlt,
                contentDescription = "Download to device",
                tint = Color.Black
            )
        }
    }
}