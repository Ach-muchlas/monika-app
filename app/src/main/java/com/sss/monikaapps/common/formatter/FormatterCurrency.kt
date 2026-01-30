package com.sss.monikaapps.common.formatter

import java.text.NumberFormat
import java.util.Locale

object FormatterCurrency {

    fun formatCurrency(amount: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp. ${formatter.format(amount)}"
    }

    fun formatCurrencyWithoutRp(amount: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return formatter.format(amount)
    }

    fun formatThousands(input: String): String {
        if (input.isBlank()) return ""

        val number = input.replace(".", "").toLongOrNull() ?: return input

        val formatter = java.text.NumberFormat.getInstance(Locale("in", "ID"))
        return formatter.format(number)
    }

}