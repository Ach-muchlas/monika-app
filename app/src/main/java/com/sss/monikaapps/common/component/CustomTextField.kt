package com.sss.monikaapps.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
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
    onNext: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    ) {
    Surface(
        shape = RoundedCornerShape(Dimens.SmallCornerRadius),
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(
            width = 1.dp,
            color = Color.Black.copy(alpha = 0.12f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            textStyle = BodyPopMedium.copy(color = textColor),
            visualTransformation = visualTransformation,

            placeholder = {
                Text(
                    text = hint,
                    style = BodyPopMedium,
                    color = Gray
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = {
                when {
                    trailingIcon != null -> trailingIcon()

                    value.isNotEmpty() && !readOnly -> {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear text",
                                tint = Gray
                            )
                        }
                    }
                }
            },

            shape = RoundedCornerShape(Dimens.SmallCornerRadius),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),

            singleLine = !isMultiline,
            maxLines = if (isMultiline) Int.MAX_VALUE else 1,

            keyboardOptions = keyboardOptions.copy(
                imeAction = if (isMultiline) ImeAction.Default else ImeAction.Next
            ),

            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() }
            ),

            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    if (readOnly && onClick != null) {
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                if (interaction is PressInteraction.Release) {
                                    onClick()
                                }
                            }
                        }
                    }
                },

            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isMultiline) Modifier.heightIn(min = 56.dp, max = 150.dp)
                    else Modifier.height(56.dp)
                )
        )
    }
}