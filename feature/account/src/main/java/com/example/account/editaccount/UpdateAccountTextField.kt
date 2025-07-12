package com.example.account.editaccount

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
/**
 * Кастомное текстовое поле для редактирования данных аккаунта.
 *
 * Особенности:
 * - Прозрачный фон без видимых границ
 * - Текст выравнивается по правому краю
 * - Поддерживает кастомизацию параметров клавиатуры
 *
 * @param value Текущее значение текстового поля
 * @param onValueChange Callback при изменении текста (String -> Unit)
 * @param keyboardOptions Опции клавиатуры (по умолчанию - стандартная клавиатура)
 *                       Для числового ввода используйте: KeyboardOptions(keyboardType = KeyboardType.Number)
 */
@Composable
fun UpdateAccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = LocalContentColor.current,
            focusedTextColor = LocalContentColor.current,
            unfocusedTextColor = LocalContentColor.current
        ),
        modifier = Modifier.width(150.dp)
    )
}