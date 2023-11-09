package com.mikali.crudplayground.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mikali.crudplayground.ui.post.model.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBodyCard(
    postItem: PostItem,
    onCardClick: (PostItem) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            onCardClick.invoke(postItem)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = postItem.title.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(text = postItem.body.orEmpty(), style = MaterialTheme.typography.bodySmall)
        }
    }


}