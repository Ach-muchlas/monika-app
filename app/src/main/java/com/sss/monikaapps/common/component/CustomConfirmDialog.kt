package com.sss.monikaapps.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.BodyPopSemiBold
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.PeachDark
import com.sss.monikaapps.common.theme.PeachLight
import com.sss.monikaapps.common.theme.PeachMid

@Composable
fun CustomConfirmDialog(
    show: Boolean,
    title: String,
    message: String,
    confirmText: String = "Iya",
    cancelText: String = "Tidak",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!show) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
        Column {
                // Title
                Text(
                    text = title,
                    fontSize = Dimens.LargeFont,
                    style = BodyPopBold
                )

                Spacer(modifier = Modifier.height(Dimens.ExtraSmallMargin))

                // Message
                Text(
                    text = message,
                    fontSize = Dimens.MediumFont,
                    color = Color.DarkGray,
                    style = BodyPopMedium
                )

                Spacer(modifier = Modifier.height(Dimens.LargeMargin))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    // Cancel Button
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = cancelText,
                            style = BodyPopSemiBold,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Confirm Button (GRADIENT)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        PeachLight,
                                        PeachMid,
                                        PeachDark
                                    ),
                                    center = Offset(0.35f, 0.25f),
                                    radius = 280f
                                )
                            )
                            .clickable { onConfirm() }
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = confirmText,
                            color = Color.White,
                            style = BodyPopBold,
                        )
                    }
                }
            }
        }
    }
}
