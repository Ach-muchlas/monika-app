package com.sss.monikaapps.feature.download.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sss.monikaapps.common.theme.BodyPopBold
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Primary
import com.sss.monikaapps.feature.download.data.entity.ConfigDownloadDataEntity

@Composable
fun CustomTableDownload(
    data: List<ConfigDownloadDataEntity>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(Dimens.SmallCornerRadius))
    ) {
        // ===== HEADER =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Primary)
                .padding(vertical = 12.dp)
        ) {
            TableCell("Nama Table", weight = 1f, isHeader = true)
            TableCell("Data Local", weight = 0.8f, isHeader = true)
            TableCell("Data Server", weight = 0.8f, isHeader = true)
            TableCell("Is Download", weight = 0.8f, isHeader = true)
        }

        Divider(color = Color.Gray)

        // ===== ROWS =====
        data.forEachIndexed { index, item ->
            val bgColor = if (index % 2 == 0) Color.White else Color(0xFFF5F5F5)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .padding(vertical = 10.dp)
            ) {
                TableCell(item.tableName, weight = 1f)
                TableCell(item.totalDataMobile.toString(), weight = 0.8f)
                TableCell(item.totalDataServer.toString(), weight = 0.8f)
                TableCell(item.statusTotalDownload.toString(), weight = 0.8f)
            }

            Divider(color = Color(0xFFE0E0E0))
        }
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false,
) {
    Text(
        text = text,
        style = if (isHeader) BodyPopBold else BodyPopMedium,
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 12.dp),
        fontSize = if (isHeader) 14.sp else 13.sp,
        color = if (isHeader) Color.White else Color.Black
    )
}
