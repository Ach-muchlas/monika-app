package com.sss.monikaapps.common.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Gray
import com.sss.monikaapps.common.theme.Primary

@Composable
fun CustomSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isValid = query.length >= 3

    val borderColor by animateColorAsState(
        targetValue = if (isValid) Primary else Gray, label = "borderColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search", tint = Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = BodyPopMedium,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Cari berdasarkan judul", color = Gray, style = BodyPopMedium
                        )
                    }
                    innerTextField()
                })

            if (query.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Gray,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            onQueryChange("")
                        })
            }
        }
    }
}
