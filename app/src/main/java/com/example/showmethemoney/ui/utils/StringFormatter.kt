package com.example.showmethemoney.ui.utils

import com.example.showmethemoney.ui.screens.transactions.TransactionItem
import com.example.showmethemoney.ui.utils.DateUtils.formatDate
import com.example.showmethemoney.ui.utils.DateUtils.formatDateForDisplay
import com.example.showmethemoney.ui.utils.DateUtils.formatTime
import com.example.showmethemoney.ui.utils.DateUtils.stringToDate
/**
 * Объект для форматирования строковых представлений финансовых данных.
 *
 * Предоставляет методы для:
 * - Форматирования денежных сумм с валютой
 * - Конвертации кодов валют в символы
 * - Форматирования списка транзакций
 * - Обработки дат транзакций
 *
 * ## Основные функции
 *
 * ### Форматирование сумм
 * - [formatAmount] - форматирует сумму с указанной валютой
 * - [formatTotalAmount] - вычисляет и форматирует общую сумму списка транзакций
 * - [formatTransactionTrail] - создает форматированную строку для отображения суммы и даты транзакции
 *
 * ### Работа с валютами
 * - [getCurrencySymbol] - возвращает символ валюты по её коду (приватный метод)
 *
 * ## Особенности форматирования
 * - Для целых чисел отображает без десятичных знаков (1000 ₽)
 * - Для дробных чисел отображает 2 знака после запятой (1000.50 ₽)
 * - Разделители тысяч заменяются на пробелы
 * - Поддерживаются основные валюты: RUB (₽), USD ($), EUR (€)
 * - Для неизвестных валют использует код валюты как есть
 * - Даты форматируются в строку с датой и временем
 */
object StringFormatter {

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

    fun formatTotalAmount(transactions: List<TransactionItem>): String {
        val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

        val currency = transactions.firstOrNull()?.accountCurrency
            ?: AccountManager.selectedAccountCurrency.value

        return formatAmount(total, currency)
    }

    fun formatTransactionTrail(item: TransactionItem): String {
        val amountStr = formatAmount(item.amount.toDoubleOrNull()
            ?: 0.0, AccountManager.selectedAccountCurrency.value)
        val dateStr = item.createdAt.let { createdAt ->
            stringToDate(createdAt)?.let { date ->
                "${formatDateForDisplay(formatDate(date))} ${formatTime(date)}"
            } ?: ""
        }
        val trailText = buildString {
            append(amountStr)
            append("\n")
            append(dateStr)
        }
        return trailText
    }
}