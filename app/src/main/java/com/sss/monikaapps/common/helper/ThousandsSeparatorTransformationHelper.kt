package com.sss.monikaapps.common.helper

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

class ThousandsSeparatorTransformationHelper : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        if (text.text.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val original = text.text

        val number = original.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)

        val formatted = NumberFormat.getInstance(Locale("in", "ID")).format(number)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return calculateTransformedOffset(original, formatted, offset)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return calculateOriginalOffset(original, formatted, offset)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    private fun calculateTransformedOffset(original: String, formatted: String, offset: Int): Int {
        var originalIndex = 0
        var transformedIndex = 0

        while (originalIndex < offset && transformedIndex < formatted.length) {
            if (formatted[transformedIndex].isDigit()) {
                originalIndex++
            }
            transformedIndex++
        }

        return transformedIndex
    }

    private fun calculateOriginalOffset(original: String, formatted: String, offset: Int): Int {
        var originalIndex = 0
        var transformedIndex = 0

        while (transformedIndex < offset && transformedIndex < formatted.length) {
            if (formatted[transformedIndex].isDigit()) {
                originalIndex++
            }
            transformedIndex++
        }

        return originalIndex
    }
}
