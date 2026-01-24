package com.example.vibramobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibramobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContextMenu(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    onVisibleChange: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = { onVisibleChange(false) },
            sheetState = sheetState
        ) {
            SheetContent()
        }
    }
}

@Composable
fun SheetContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(2.dp)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.default_image),
            )

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    text = "Lemon",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Kenshi",
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp, bottom = 18.dp),
            thickness = 2.dp
        )

        LazyColumn {
            items(5) { index ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Item $index",
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
