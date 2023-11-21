package com.mikali.crudplayground.ui.screens.posts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.theme.mintGreen

@Composable
fun PostCard(
    postItem: PostItem,
    onOptionsClick: (PostItem) -> Unit, // Add callback for options click
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = mintGreen),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column( modifier = Modifier
                .weight(1f)
                .padding(8.dp)) {
                Text(
                    text = postItem.title.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = postItem.body.orEmpty(),
                    fontSize = 14.sp
                )
            }

            IconButton(
                onClick = {
                    onOptionsClick.invoke(postItem)
                },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert, // Default icon for three dots
                    contentDescription = "Options"
                )
            }
        }
    }
}