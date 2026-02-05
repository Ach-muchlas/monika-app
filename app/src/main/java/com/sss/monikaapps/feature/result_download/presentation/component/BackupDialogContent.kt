package com.sss.monikaapps.feature.result_download.presentation.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sss.monikaapps.common.component.CustomPrimaryButton
import com.sss.monikaapps.common.component.CustomTextField
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.TitlePopBold


@Composable
fun BackupDialogContent(
    reason: String,
    modifier: Modifier = Modifier,
    onReasonChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onClose: () -> Unit,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(20.dp)
    ) {

        // ❌ Close Button
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🟡 Title
        Text(
            text = "Backup",
            style = TitlePopBold.copy(
                fontSize = 18.sp,
                color = Color(0xFFB08A2E)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 📝 Label
        Text(
            text = "Alasan Backup",
            style = BodyPopMedium.copy(fontSize = 14.sp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✏️ TextField
        CustomTextField(
            value = reason,
            onValueChange = onReasonChange,
            hint = "Masukan alasan backup",
            isMultiline = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🟠 Button
        CustomPrimaryButton(
            text = "Kirim",
            enabled = reason.isNotBlank(),
            isLoading = isLoading,
            onClick = onSubmit
        )
    }
}
