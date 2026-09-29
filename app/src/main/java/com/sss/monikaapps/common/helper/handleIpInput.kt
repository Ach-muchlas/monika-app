package com.sss.monikaapps.common.helper

import androidx.compose.ui.focus.FocusRequester

fun handleIpInput(
    newValue: String,
    onValueChange: (String) -> Unit,
    nextFocus: FocusRequester?
) {
    val filtered = newValue.filter { it.isDigit() }.take(3)
    onValueChange(filtered)

    if (filtered.length == 3) {
        nextFocus?.requestFocus()
    }
}
