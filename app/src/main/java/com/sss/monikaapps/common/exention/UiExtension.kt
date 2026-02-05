package com.sss.monikaapps.common.exention

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object UiExtension {
    fun Modifier.singleClick(
        enabled: Boolean = true,
        debounceTime: Long = 600L,
        onClick: () -> Unit,
    ): Modifier = composed {

        val scope = rememberCoroutineScope()
        var clickableEnabled by remember { mutableStateOf(true) }

        clickable(enabled = enabled && clickableEnabled) {
            if (!clickableEnabled) return@clickable

            clickableEnabled = false
            onClick()

            scope.launch {
                delay(debounceTime)
                clickableEnabled = true
            }
        }
    }


}