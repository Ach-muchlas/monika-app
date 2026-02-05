package com.sss.monikaapps.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sss.monikaapps.common.theme.BodyPopMedium
import com.sss.monikaapps.common.theme.Dimens
import com.sss.monikaapps.common.theme.Gray

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    textColor: Color = Color.Black,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    isMultiline: Boolean = false,
    showClearIcon: Boolean = true,
    onNext: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    paddingEnd: Dp = Dimens.MediumMargin,
    paddingStart: Dp = Dimens.MediumMargin,
) {
    val interactionSource = remember { MutableInteractionSource() }

    if (readOnly && onClick != null) {
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                if (interaction is PressInteraction.Release) {
                    onClick()
                }
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(Dimens.SmallCornerRadius),
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(
            width = 1.dp, color = Color.Black.copy(alpha = 0.12f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            textStyle = BodyPopMedium.copy(color = textColor),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions.copy(
                imeAction = if (isMultiline) ImeAction.Default else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() }),
            interactionSource = interactionSource,
            singleLine = !isMultiline,
            maxLines = if (isMultiline) Int.MAX_VALUE else 1,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isMultiline) Modifier.heightIn(min = 56.dp, max = 150.dp)
                    else Modifier.height(56.dp)
                ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            PaddingValues(
                                start = paddingStart,
                                top = 16.dp,
                                end = paddingEnd,
                                bottom = 16.dp
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Leading Icon
                    leadingIcon?.let {
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            it()
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint, style = BodyPopMedium, color = Gray
                            )
                        }
                        innerTextField()
                    }

                    // Trailing Icon
                    when {
                        trailingIcon != null -> {
                            Box(modifier = Modifier.padding(start = 8.dp)) {
                                trailingIcon()
                            }
                        }

                        showClearIcon && value.isNotEmpty() && !readOnly -> {
                            IconButton(
                                onClick = { onValueChange("") },
                                modifier = Modifier
                                    .size(26.dp)
                                    .padding(start = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear text",
                                    tint = Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            })
    }
}