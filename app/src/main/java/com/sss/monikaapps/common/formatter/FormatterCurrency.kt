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

    fun cleanCurrency(input: String): Long {
        if (input.isBlank()) return 0L
        val cleaned = input.replace("[^\\d]".toRegex(), "")
        return cleaned.toLongOrNull() ?: 0L
    }

}