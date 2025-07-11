package com.example.transactions.addtransaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.R
import com.example.ui.UniversalListItem
import com.example.ui.theme.Red

@Composable
fun AddTransactionList(
    isIncome: Boolean,
    state: TransactionUiState,
    onCategoryClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onCommentChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    currentTransactionId: Int?
) {
    val isVisible = currentTransactionId != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp)
    ) {
        LazyColumn(Modifier.weight(1f)) {
            item {
                UniversalListItem(
                    content = "Счет" to null,
                    trail = state.accountName to null
                )
            }
            item {
                UniversalListItem(
                    content = "Статья" to null,
                    trail = state.categoryName to {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vert),
                            contentDescription = "Выбрать категорию",
                            modifier = Modifier
                                .size(24.dp),
                        )
                    },
                    onClick = onCategoryClick
                )
            }
            item {
                UniversalListItem(
                    content = "Сумма" to null,
                    trail = null to {
                        BasicTextField(
                            value = state.amount,
                            onValueChange = onAmountChange,
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                )
            }
            item {
                UniversalListItem(
                    content = "Дата" to null,
                    trail = state.getFormattedDate() to null,
                    onClick = onDateClick
                )
            }
            item {
                UniversalListItem(
                    content = "Время" to null,
                    trail = state.getFormattedTime() to null,
                    onClick = onTimeClick
                )
            }
            item {
                UniversalListItem(
                    content = "Комментарий" to null,
                    trail = null to {
                        BasicTextField(
                            value = state.comment ?: "",
                            onValueChange = onCommentChange,
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                        )
                    }
                )
            }
            if (isVisible) {
                item {
                    Button(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        )
                    ) {
                        Text(
                            text = if (isIncome) "Удалить доход" else "Удалить расход",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}