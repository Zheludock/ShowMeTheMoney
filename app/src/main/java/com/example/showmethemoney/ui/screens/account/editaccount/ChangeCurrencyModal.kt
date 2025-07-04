package com.example.showmethemoney.ui.screens.account.editaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.showmethemoney.R
import com.example.showmethemoney.ui.theme.Red
import com.example.showmethemoney.ui.theme.SelectedTextUnderIcons
import com.example.showmethemoney.ui.theme.White
import com.example.showmethemoney.ui.theme.WhiteBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeCurrencyModal(
    onElementSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(),
        scrimColor = Color(0x52000000),
        containerColor = WhiteBG,
        modifier = Modifier.background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ModalList(icon = {
                Icon(
                    painterResource(R.drawable.ic_ruble),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "RUB"
                )
            },
                text = "Российский рубль",
                onClick = { onElementSelected("RUB") })
            ModalList(
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_dollar),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "USD"
                    )
                },
                text = "Американский доллар",
                onClick = { onElementSelected("USD") })
            ModalList(
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_euro),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "EUR"
                    )
                },
                text = "Евро",
                onClick = { onElementSelected("EUR") })
            ModalList(
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_close_in_round),
                        modifier = Modifier.size(24.dp),
                        tint = White,
                        contentDescription = "cancel"
                    )
                },
                text = "Отмена",
                onClick = { onDismissRequest() },
                backgroundColor = Red
            )
        }
    }
}

@Composable
fun ModalList(
    icon: @Composable (() -> Unit),
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color.Transparent,
) {
    Row(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            icon.invoke()
        }
        Text(
            text = text,
            color = if (text != "Отмена") SelectedTextUnderIcons else White
        )
    }
}