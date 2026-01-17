package com.sss.monikaapps.common.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.sss.monikaapps.common.theme.BodyPopBold
import kotlinx.coroutines.delay

@Composable
fun CustomAnimatedDotText(
    baseText: String = "Loading",
    dotCount: Int = 3,
    intervalMs: Long = 500L,
) {
    var dots by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(intervalMs)
            dots = (dots + 1) % (dotCount + 1)
        }
    }

    Text(
        text = baseText + ".".repeat(dots),
        style = BodyPopBold,
        textAlign = TextAlign.Center
    )
}
