package com.sss.monikaapps.feature.photo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.sss.monikaapps.R
import com.sss.monikaapps.common.component.CustomOutlineButton
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray

@Composable
fun CustomMultiPhotoCard(
    photos: List<String>,
    modifier: Modifier = Modifier,
    title: String = "Masukkan foto aktivitas",
    onAddPhoto: () -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 6.dp,
        shadowElevation = 10.dp,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.08f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0xFFF3F6FA))
                    )
                )
                .padding(12.dp)
        ) {

            if (photos.isEmpty()) {
                // ===== EMPTY STATE =====
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_add_photo),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                    )

                    Spacer(Modifier.height(Dimens.SmallMargin))

                    Text(title, style = BodyPopMedium, color = Gray)

                    Spacer(Modifier.height(Dimens.SmallMargin))

                    CustomOutlineButton(
                        "Ambil Foto",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(40.dp)
                    ) {
                        onAddPhoto()
                    }
                }
            } else {
                // ===== GRID FOTO =====

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(photos) { photo ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray)
                        ) {
                            AsyncImage(
                                model = photo.toUri(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // ❌ Delete button
                            IconButton(
                                onClick = { onDeletePhoto(photo) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(28.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // ➕ ADD BUTTON
                    item {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    2.dp,
                                    Color.Gray.copy(alpha = 0.4f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onAddPhoto() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
