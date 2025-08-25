package com.nsutrack.financetracker.ui.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN")) as DecimalFormat
    val symbols = format.decimalFormatSymbols
    symbols.currencySymbol = "₹"
    format.decimalFormatSymbols = symbols
    return format.format(amount)
}
