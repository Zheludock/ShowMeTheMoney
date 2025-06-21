package com.example.showmethemoney.ui.utils

fun formatAmount(amount: Double, currency: String): String {
    val formattedAmount = if (amount % 1 == 0.0) {
        "%,d".format(amount.toLong()).replace(',', ' ')
    } else {
        "%,.2f".format(amount).replace(',', ' ')
    }

    return "$formattedAmount ${getCurrencySymbol(currency)}"
}

fun getCurrencySymbol(currencyCode: String): String {
    return when (currencyCode.uppercase()) {
        "RUB" -> "\u20BD" // ₽
        "USD" -> "\u0024" // $
        "EUR" -> "\u20AC" // €
        else -> currencyCode
    }
}