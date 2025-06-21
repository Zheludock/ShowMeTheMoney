package com.example.showmethemoney.data.dto.account

data class UpdateAccountRequest(
    val name: String?,
    val balance: String?,
    val currency: String?
)