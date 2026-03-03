package com.sss.monikaapps.feature.connection.presentation.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.sss.monikaapps.common.component.CustomTextField

@Composable
fun IpTextField(
    value: String,
    hint: String,
    focusRequester: FocusRequester,
    nextFocus: FocusRequester?,
    onValueChangeState: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomTextField(
        value = value,
        hint = hint,
        modifier = modifier
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        showClearIcon = false,
        onValueChange = {
            val filtered = it.filter { c -> c.isDigit() }.take(3)
            onValueChangeState(filtered)

            if (filtered.length == 3) {
                val intVal = filtered.toIntOrNull()
                if (intVal != null && intVal in 0..255) {
                    nextFocus?.requestFocus()
                }
            }
        }
    )
}
